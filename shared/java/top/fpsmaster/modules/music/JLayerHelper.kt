package top.fpsmaster.modules.music

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D
import javazoom.jl.converter.Converter
import javazoom.jl.decoder.JavaLayerException
import java.io.File
import java.io.IOException
import javax.sound.sampled.*
import kotlin.math.min
import kotlin.math.sqrt

object JLayerHelper {
    var clip: Clip? = null
    var audIn: AudioInputStream? = null
    var audioBytes: ByteArray = ByteArray(0)
    var loudnessCurve: DoubleArray? = DoubleArray(0)
    val progress: Float
        get() {
            val timeElapsed = clip!!.microsecondPosition
            val total = clip!!.microsecondLength
            return timeElapsed.toFloat() / total
        }

    fun playWAV(wavFile: String) {
        // Open an audio input stream.
        val soundFile = File(wavFile) //you could also get the sound file with an URL
        var audioIn = AudioSystem.getAudioInputStream(soundFile)
        audIn = AudioSystem.getAudioInputStream(soundFile)
        audioBytes = readAudioData(audIn!!)
        // Get a sound clip resource.
        clip = AudioSystem.getClip()
        // Open audio clip and load samples from the audio input stream.
        if (clip == null)
            return
        clip!!.open(audioIn)
        clip!!.start()
    }

    @JvmStatic
    fun seek(progress: Float) {
        val totalTime = clip!!.microsecondLength
        val currentTime = (totalTime * progress).toLong() //将播放进度设置为50%
        clip!!.microsecondPosition = currentTime
    }

    fun updateLoudness() {
        if (clip == null || audIn == null) return

        val format = audIn!!.format
        if (format.encoding != AudioFormat.Encoding.PCM_SIGNED || format.sampleSizeInBits != 16) {
            println("Unsupported format: $format")
            return
        }

        val currentTimeSec = clip!!.microsecondPosition / 1_000_000.0
        val fftSize = 1024
        val fftWindowDuration = fftSize / format.sampleRate

        val audioSegment = getAudioSegment(
            audioBytes,
            format.sampleRate,
            format.frameSize,
            (currentTimeSec - fftWindowDuration / 2).toFloat(),
            fftWindowDuration.toFloat()
        )

        val fftData = performFFT(audioSegment)
        val amplitudes = computeAmplitude(fftData)

        loudnessCurve = amplitudes
    }


    @Throws(IOException::class)
    fun readAudioData(audioInputStream: AudioInputStream): ByteArray {
        val bufferSize = (audioInputStream.frameLength * audioInputStream.format.frameSize).toInt()
        val audioBytes = ByteArray(bufferSize)
        audioInputStream.read(audioBytes)
        return audioBytes
    }

    fun getAudioSegment(
        audioData: ByteArray,
        sampleRate: Float,
        bytesPerFrame: Int,
        startSecond: Float,
        durationInSeconds: Float
    ): ByteArray {
        val startSample = (startSecond * sampleRate).toInt().coerceAtLeast(0)
        val numSamples = (durationInSeconds * sampleRate).toInt()

        val startByte = startSample * bytesPerFrame
        val numBytes = numSamples * bytesPerFrame

        val endByte = (startByte + numBytes).coerceAtMost(audioData.size)

        return audioData.copyOfRange(startByte, endByte)
    }



    private fun performFFT(buffer: ByteArray): DoubleArray {
        val numSamples = buffer.size / 2
        val audioData = DoubleArray(numSamples)

        // Convert byte pairs (little-endian) to signed 16-bit samples
        for (i in 0 until numSamples) {
            val low = buffer[i * 2].toInt() and 0xff
            val high = buffer[i * 2 + 1].toInt()
            val sample = (high shl 8) or low
            audioData[i] = sample / 32768.0 // Normalize to [-1, 1]
        }

        // Zero-padding to nearest power of 2 (optional, or cut to fixed size like 1024)
        val fftSize = 1024
        val paddedData = DoubleArray(fftSize)
        for (i in 0 until min(fftSize, audioData.size)) {
            paddedData[i] = audioData[i]
        }

        val fft = DoubleFFT_1D(fftSize)
        fft.realForward(paddedData)

        return paddedData
    }



    fun computeAmplitude(fftData: DoubleArray): DoubleArray {
        val n = fftData.size
        val amplitudes = DoubleArray(n / 2)

        for (i in amplitudes.indices) {
            val real = fftData[2 * i]
            val imag = if (2 * i + 1 < fftData.size) fftData[2 * i + 1] else 0.0
            amplitudes[i] = sqrt(real * real + imag * imag)
        }

        // 简单归一化
        val maxAmp = amplitudes.maxOrNull() ?: 1.0
        return amplitudes.map { it / maxAmp }.toDoubleArray()
    }


    fun setVolume(vol: Float) {
        var vol = vol
        vol /= 2
        vol += 0.5f
        val volumeControl = clip!!.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
        // Change the volume to half way between minimum and maximum
        val volume = (volumeControl.maximum - volumeControl.minimum) * vol + volumeControl.minimum
        volumeControl.value = volume
    }

    fun convert(sourcePath: String, targetPath: String) {
        try {
            val converter = Converter()
            val sourceFile = File(sourcePath)
            val targetFile = File(targetPath)
            converter.convert(sourceFile.path, targetFile.path)
        } catch (e: JavaLayerException) {
            e.printStackTrace()
        }
    }

    fun stop() {
        clip!!.stop()
    }

    fun start() {
        clip!!.start()
    }

    @JvmStatic
    val duration: Double
        get() = (clip!!.microsecondLength / 1000f / 1000f / 60f).toDouble()
}
