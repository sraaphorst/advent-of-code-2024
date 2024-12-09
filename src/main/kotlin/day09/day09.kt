// Advent of Code 2024, Day 09.
// By Sebastian Raaphorst, 2024.

package day09

import common.aocreader.fetchAdventOfCodeInput

private typealias Unused = IntRange
data class File(val idx: Int, var blocks: MutableList<IntRange>)

// Data format:
// file0 freespace file1 freespace...
fun parse(input: String): Pair<List<File>, MutableList<Unused>> {
    tailrec fun aux(
        remainingString: String = input,
        fileNumber: Int = 0,
        currentBlock: Int = 0,
        files: List<File> = listOf(),
        gaps: MutableList<Unused> = mutableListOf()
    ): Pair<List<File>, MutableList<Unused>> = when {
        remainingString.isEmpty() -> files to gaps
        else -> {
            val fileSize = remainingString[0].digitToInt()
            val file = File(fileNumber, mutableListOf(currentBlock until (currentBlock + fileSize)))
            val nextBlock = currentBlock + fileSize - 1

            val gapSize = remainingString.getOrNull(1)?.digitToInt() ?: 0
            if (gapSize > 0)
                gaps.add(nextBlock + 1 until (nextBlock + gapSize + 1))

            aux(
                remainingString.drop(2),
                fileNumber + 1,
                nextBlock + 1 + gapSize,
                files + file,
                gaps
            )
        }
    }
    return aux()
}

private fun mergeRanges(ranges: MutableList<Unused>): MutableList<Unused> {
    if (ranges.isEmpty()) return mutableListOf()

    // Sort the ranges by their start values
    val sortedRanges = ranges.sortedBy { it.first }
    val merged = mutableListOf<IntRange>()

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



// Gaps are already sorted.
// Files are sorted in reverse order.
private fun consolidateFiles(files: List<File>, gaps: MutableList<Unused>): List<File> {
    var currentGaps = gaps
    for (file in files.reversed()) {
        // Calculate the new range of blocks for the file.
        val newBlocks = mutableListOf<IntRange>()

        for (block in file.blocks.reversed()) {
            var remainingSize = block.last - block.first + 1 // Size of the current block
            while (remainingSize > 0 && currentGaps.isNotEmpty()) {
                currentGaps = mergeRanges(currentGaps)
                val gap = currentGaps.first()
                val gapSize = gap.last - gap.first + 1
                println("Moving file ${file.idx} block ${block.last - gapSize}..${block.last} to ${gap}")

                if (gapSize >= remainingSize) {
                    // We can place all peeled blocks into this gap starting at gap.first
                    val end = gap.first + remainingSize - 1
                    newBlocks.add(gap.first..end)
//                    currentGaps.add(block)

                    // Update or remove the gap accordingly
                    if (gapSize > remainingSize) {
                        // Shrink gap from the left
                        currentGaps[0] = (end + 1)..gap.last
                    } else {
                        // Fully consumed the gap
                        currentGaps.removeAt(0)
                    }

                    remainingSize = 0
                } else {
                    // We can't fit all here, use the entire gap
                    newBlocks.add(gap)
                    currentGaps.removeAt(0)
                    remainingSize -= gapSize
                }
                println("Now:")
                println("currentGaps: $currentGaps")
                println("newBlocks: $newBlocks")
            }

            if (remainingSize > 0) {
                // Any leftover remains at the low end of the original block
                val leftoverEnd = block.first + remainingSize - 1
                newBlocks.add(block.first..leftoverEnd)
            }
        }

        // Update the file's blocks
        file.blocks = newBlocks
    }

    // Reconstruct the drive layout
    val driveSize = files.flatMap { it.blocks }.maxOf { it.last } + 1
    val drive = CharArray(driveSize) { '.' }

    for (file in files) {
        for (block in file.blocks) {
            for (i in block) {
                drive[i] = '0' + file.idx
            }
        }
    }

    println(String(drive))

    // Reconstruct the drive layout
    return files
}


private fun checksum(files: List<File>): Int =
    files.sumOf { file -> file.blocks.sumOf { file.idx * it.sum() }}


fun answer1(input: String): Int =
    parse(input)
        .let { consolidateFiles(it.first, it.second) }
        .let(::checksum)

fun answer2(input: String): Int =
    TODO()


fun main() {
//    val input2 = "2333133121414131402"
    val input2 = "12345"
    val (files2, gaps2) = parse(input2)
    consolidateFiles(files2, gaps2)
    println("\n\n\n")
    val input = fetchAdventOfCodeInput(2024, 9)
//    val input = "2333133121414131402"
    println("--- Day 9: Disk Fragmenter ---")

//    val files = listOf(
//        File(0, mutableListOf(0..1)),
//        File(1, mutableListOf(5..7)),
//        File(2, mutableListOf(11..11)),
//        File(3, mutableListOf(15..17)),
//        File(4, mutableListOf(19..20)),
//        File(5, mutableListOf(22..25)),
//        File(6, mutableListOf(27..30)),
//        File(7, mutableListOf(32..34)),
//        File(8, mutableListOf(36..39)),
//        File(9, mutableListOf(40..41))
//    )
//    val gaps = mutableListOf(2..4, 8..10, 12..14, 18..18, 21..21, 26..26, 31..31, 35..35)
//
//    (0..41).forEach { i -> print(i / 10)}
//    println()
//    (0..41).forEach { i -> print(i % 10)}
//    println()
//    (0..41).forEach { print("-") }
//    println()
//    val result = consolidateFiles(files, gaps)
//    println("0099811188827773336446555566..............")
//    println(result)
//    // Part 1:
//    println("Part 1: ${answer1(input)}")

    // Part 2:
//    println("Part 2: ${answer2(input)}")
}
