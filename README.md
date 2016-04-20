# HuffmanCoding
My attempt at creating a program that simulates a compression of a text file using Huffman Encoding.
Note that it does not actually compress files as when I write "binary" to a file each 1 and 0 is written as a byte and not a bit.
While doing this assignment I used/modified/referred to code from these places: https://rosettacode.org/wiki/Huffman_coding#Java , 
http://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/ , http://algs4.cs.princeton.edu/55compression/Huffman.java.html
 
To test it simply add your own text to the sampleFile.txt or use the text provided and see the two files that it creates and the output
to the console. It should create a file called Encoded.txt and Decoded.txt. 
Encoded.txt will contain a text version of the HashMap used to encode the file which will then be rebuilt back into a HashMap and used
to rebuild the original text of the document which is coded as "Huffman binary" after the HashMap in Encoded.txt.
Decoded.txt should contain the same text as the orginal sampleFile.txt because it has undergone the same process of encoding and decoding
so it should be identical apart from any line breaks that were in the original.
