// Advent of Code 2024, Day 09.
// By Sebastian Raaphorst, 2024.

package day09

import common.aocreader.fetchAdventOfCodeInput
import java.math.BigInteger

private typealias Range = LongRange
private typealias RangeList = List<Range>

private fun Range.size(): Long = last - first + 1

private data class File(val id: Int, val blocks: RangeList)

private data class Filesystem(val files: List<File>, val gaps: RangeList) {
    fun sortAndMergeRanges(ranges: RangeList): RangeList {
        if (ranges.isEmpty()) return emptyList()

        val sorted = ranges.sortedBy { it.first }
        return sorted.fold(mutableListOf<Range>()) { acc, range ->
            if (acc.isEmpty()) {
                acc += range
            } else {
                val lastRange = acc.last()
                if (range.first <= lastRange.last + 1) {
                    acc[acc.lastIndex] = lastRange.first..maxOf(lastRange.last, range.last)
                } else {
                    acc += range
                }
            }
            acc
        }
    }

    private fun sortFiles(files: List<File>): List<File> =
        files.sortedBy { it.blocks.maxOfOrNull { block -> block.last } }

    private fun sortFile(file: File): File =
        file.copy(blocks = file.blocks.sortedBy { it.last })

    fun rearrange1(): Filesystem {
        tailrec fun loop(fs: Filesystem): Filesystem {
            val files = fs.files
            val gaps = fs.gaps

            val lastFile = files.lastOrNull() ?: return fs
            val lastBlock = lastFile.blocks.lastOrNull() ?: return fs
            val firstGap = gaps.firstOrNull() ?: return fs

            // Done if the first gap is after the last block
            if (firstGap.first >= lastBlock.last) return fs

            val blockSize = lastBlock.size()
            val gapSize = firstGap.size()

            val (newFiles, newGaps) = when {
                gapSize == blockSize -> {
                    // Same size: Swap block and gap
                    val updatedFile = lastFile.copy(blocks = (lastFile.blocks - listOf(lastBlock)) + listOf(firstGap))
                    val updatedFiles = files.dropLast(1) + sortFile(updatedFile)
                    val updatedGaps = (gaps - listOf(firstGap)) + listOf(lastBlock)
                    updatedFiles to updatedGaps
                }
                gapSize > blockSize -> {
                    // Gap larger than block: split gap into two
                    val dividingPoint = firstGap.first + blockSize
                    val subGap1 = firstGap.first until dividingPoint
                    val subGap2 = dividingPoint..firstGap.last

                    val updatedFile = lastFile.copy(blocks = (lastFile.blocks - listOf(lastBlock)) + listOf(subGap1))
                    val updatedFiles = files.dropLast(1) + sortFile(updatedFile)
                    val updatedGaps = (gaps - listOf(firstGap)) + listOf(subGap2, lastBlock)
                    updatedFiles to updatedGaps
                }
                else -> {
                    // Block larger than gap: split block into two
                    val dividingPoint = lastBlock.last - gapSize + 1
                    val subBlock1 = lastBlock.first until dividingPoint
                    val subBlock2 = dividingPoint..lastBlock.last

                    val updatedFile = lastFile.copy(blocks = (lastFile.blocks - listOf(lastBlock)) + listOf(firstGap, subBlock1))
                    val updatedFiles = files.dropLast(1) + sortFile(updatedFile)
                    val updatedGaps = (gaps - listOf(firstGap)) + listOf(subBlock2)
                    updatedFiles to updatedGaps
                }
            }

            val mergedFiles = sortFiles(newFiles).map { f -> f.copy(blocks = sortAndMergeRanges(f.blocks)) }
            val mergedGaps = sortAndMergeRanges(newGaps)

            return loop(Filesystem(mergedFiles, mergedGaps))
        }

        return loop(this)
    }

    fun rearrange2(): Filesystem {
        // For each file from the end, try to move it into a suitable gap
        val reversed = files.asReversed()
        val updated = reversed.fold(this) { acc, file ->
            val fileRange = file.blocks.lastOrNull() ?: return@fold acc
            val fileRangeSize = fileRange.size()

            // Find the first suitable gap
            val gap = acc.gaps.firstOrNull { g ->
                g.size() >= fileRangeSize && g.first < fileRange.first
            }

            if (gap == null) {
                acc
            } else {
                val updatedFile: File
                val updatedGaps: RangeList
                val gapSize = gap.size()

                when {
                    fileRangeSize == gapSize -> {
                        // Same size: just replace
                        updatedFile = file.copy(blocks = listOf(gap))
                        updatedGaps = acc.gaps - listOf(gap)
                    }
                    fileRangeSize < gapSize -> {
                        val dividingPoint = gap.first + fileRangeSize
                        val gap1 = gap.first until dividingPoint
                        val gap2 = dividingPoint..gap.last
                        updatedFile = file.copy(blocks = listOf(gap1))
                        updatedGaps = (acc.gaps - listOf(gap)) + listOf(gap2)
                    }
                    else -> {
                        // Should not happen as per the logic, but just in case
                        updatedFile = file
                        updatedGaps = acc.gaps
                    }
                }

                val newFiles = acc.files.map {
                    if (it.id == file.id) updatedFile else it
                }
                Filesystem(sortFiles(newFiles), sortAndMergeRanges(updatedGaps))
            }
        }

        return updated
    }

    fun checksum(): BigInteger =
        files.fold(BigInteger.ZERO) { acc, (idx, blocks) ->
            acc + blocks.fold(BigInteger.ZERO) { acc2, range ->
                acc2 + (range.fold(BigInteger.ZERO) { acc3, blockPos ->
                    acc3 + (idx * blockPos).toBigInteger()
                })
            }
        }

    companion object {
        private fun isFile(idx: Int) = idx % 2 == 0
        private fun isSpace(idx: Int) = idx % 2 == 1

        fun parse(input: String): Filesystem {
            val parsed = input.withIndex().fold(
                Triple(mutableListOf<File>(), mutableListOf<Range>(), 0L)
            ) { (files, spaces, currBlock), (idx, ch) ->
                val length = ch.digitToInt()
                if (length > 0) {
                    val range: Range = currBlock until (currBlock + length)
                    when {
                        isFile(idx) -> files.add(File(idx / 2, listOf(range)))
                        isSpace(idx) -> spaces.add(range)
                    }
                    Triple(files, spaces, currBlock + length)
                } else {
                    Triple(files, spaces, currBlock)
                }
            }

            val (files, gaps, _) = parsed
            val fs = Filesystem(files, gaps)
            // Sort everything before returning
            return fs.copy(files = fs.sortFiles(fs.files), gaps = fs.sortAndMergeRanges(fs.gaps))
        }
    }
}

fun answer1(input: String): BigInteger =
    Filesystem.parse(input).rearrange1().checksum()

fun answer2(input: String): BigInteger =
    Filesystem.parse(input).rearrange2().checksum()

fun main() {
    val input = fetchAdventOfCodeInput(2024, 9)

    println("--- Day 9: Disk Fragmenter ---")

    // Part 1: 6384282079460
    println("Part 1: ${answer1(input)}")

    // Part 2: 6408966547049
    println("Part 2: ${answer2(input)}")
}
