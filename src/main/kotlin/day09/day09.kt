// Advent of Code 2024, Day 09.
// By Sebastian Raaphorst, 2024.

package day09

import common.aocreader.fetchAdventOfCodeInput
import java.math.BigInteger

typealias Range = LongRange
typealias RangeList = MutableList<Range>

private fun Range.size(): Long =
    last - first + 1

data class File(val id: Int, var blocks: RangeList)

class Filesystem(var files: MutableList<File>, var gaps: RangeList) {
    init {
        sortGaps()
        sortFiles()
    }

    // The gaps need to be sorted by the first block they occupy.
    private fun sortGaps() {
        gaps.sortBy { g -> g.first }
    }

    // The files need to be sorted by the last block they occupy.
    private fun sortFiles() {
        files.sortBy { file -> file.blocks.maxOfOrNull(Range::last) }
    }

    // Sorts the file so that the highest range is in the last block.
    private fun sortFile(file: File) {
        file.blocks.sortBy(Range::last)
    }

    /**
     * Reparse before using this.
     */
    fun rearrange1() {
        // We want to get the highest block and move it to the earliest position if
        // it makes sense to do so.
        while (true) {
            // Get the last file, which we want to move earlier if possible, and the
            // first gap, which is where we want to put it at.
            var lastFile = files.lastOrNull() ?: break
            val lastBlock = lastFile.blocks.lastOrNull() ?: break
            var firstGap = gaps.firstOrNull() ?: break

            // We are done if the first gap is before the end of the last file.
            if (firstGap.first >= lastBlock.last)
                break

            // Otherwise we have three cases.
            val blockSize = lastBlock.size()
            val gapSize = firstGap.size()

            when {
                gapSize == blockSize -> {
                    // The sizes are the same, in which case we flip them.
                    gaps -= firstGap
                    lastFile.blocks -= lastBlock
                    lastFile.blocks += firstGap
                }

                gapSize > blockSize -> {
                    // If the gap is bigger than the block, we divide the gap in two
                    // and move the block to the first part of the gap.
                    val dividingPoint = firstGap.first + blockSize
                    val subGap1 = firstGap.first until dividingPoint
                    val subGap2 = dividingPoint..firstGap.last

                    lastFile.blocks -= lastBlock
                    lastFile.blocks += subGap1

                    gaps -= firstGap
                    gaps += subGap2
                    gaps += lastBlock
                }

                gapSize < blockSize -> {
                    // If the block is bigger than the gap, we divide the block in two
                    // and move the last part to the gap.
                    val dividingPoint = lastBlock.last - gapSize + 1
                    val subBlock1 = lastBlock.first until dividingPoint
                    val subBlock2 = dividingPoint..lastBlock.last

                    lastFile.blocks -= lastBlock
                    lastFile.blocks += firstGap
                    lastFile.blocks += subBlock1

                    gaps -= firstGap

                    // Do we need this?
                    gaps += subBlock2
                }
            }

            // Re-sort.
            sortFile(lastFile)
            mergeRanges(lastFile.blocks).let { lastFile.blocks = it }
            sortFiles()
            sortGaps()
            gaps = mergeRanges(gaps)
        }
    }

    /**
     * Reparse before using this.
     */
    fun rearrange2() {
        for (file in files.reversed()) {
            // At this point, each file only has one range.
            val fileId = file.id
            val fileRange = file.blocks.lastOrNull() ?: error("No data for $fileId")
            val fileRangeSize = fileRange.size()

            // Find the first gap it will fit.
            val gap = gaps.firstOrNull { gap -> gap.size() >= fileRangeSize && gap.start < fileRange.start} ?: continue
            val gapSize = gap.size()

            when {
                fileRangeSize == gapSize -> {
                    // The files are the same size. Just move the file to the gap.
                    file.blocks = mutableListOf(gap)
                    gaps.remove(gap)
                }
                fileRangeSize < gapSize -> {
                    val dividingPoint = gap.start + fileRangeSize
                    val gap1 = gap.start until dividingPoint
                    val gap2 = dividingPoint..gap.last

                    file.blocks = mutableListOf(gap1)
                    gaps.remove(gap)
                    gaps.add(gap2)
                }
            }

            // Sort the gaps.
            sortGaps()
            gaps = mergeRanges(gaps)
        }
    }

    fun mergeRanges(ranges: MutableList<Range>): MutableList<Range> {
        if (ranges.isEmpty()) return mutableListOf()

        // Sort the ranges by their start values
        val sortedRanges = ranges.sortedBy { it.first }
        val merged = mutableListOf<Range>()

        var currentRange = sortedRanges.first()

        for (range in sortedRanges.drop(1)) {
            if (range.first <= currentRange.last + 1) {
                // Merge ranges if they overlap or are contiguous
                currentRange = currentRange.first..maxOf(currentRange.last, range.last)
            } else {
                // Add the non-overlapping range to the result
                merged.add(currentRange)
                currentRange = range
            }
        }

        // Add the final range
        merged.add(currentRange)

        return merged
    }

    fun checksum(): BigInteger =
        files.sumOf { (idx, block) -> block.sumOf { pos -> pos.sumOf { idx * it }.toBigInteger() } }

    companion object {
        private fun isFile(idx: Int) = idx % 2 == 0
        private fun isSpace(idx: Int) = idx % 2 == 1

        fun parse(input: String): Filesystem {
            val files: MutableList<File> = mutableListOf()
            val space: RangeList = mutableListOf()
            var currBlock = 0L

            input.withIndex().forEach { (idx, ch) ->
                val length: Int = ch.digitToInt()

                // Skip any entries with length 0.
                if (length > 0) {
                    val range: Range = currBlock until (currBlock + length)
                    if (isFile(idx))
                        files.add(File(idx / 2, mutableListOf(range)))
                    else if (isSpace(idx))
                        space.add(range)
                    currBlock += length
                }
            }

            return Filesystem(files, space)
        }
    }
}

fun answer1(input: String): BigInteger {
    val f = Filesystem.parse(input)
    f.rearrange1()
    return f.checksum()
}

fun answer2(input: String): BigInteger {
    val f = Filesystem.parse(input)
    f.rearrange2()
    return f.checksum()
}


fun main() {
    val input = fetchAdventOfCodeInput(2024, 9)

    println("--- Day 9: Resonant Collinearity ---")

    // Part 1: 6384282079460
    println("Part 1: ${answer1(input)}")

    // Part 2: 6408966547049
    println("Part 2: ${answer2(input)}")
}
