

public class Node implements Comparable<Node>{

	private char ch;
	private int freq;
	private Node left, right;
	
	Node(char ch, int freq, Node left, Node right)
	{
		this.ch = ch;
		this.freq = freq;
		this.left = left;
		this.right = right;
	}
	
	public boolean isLeaf()
	{
		if(left == null && right == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public int compareTo(Node node)
	{
		return this.freq - node.freq;
	}
	
	public int getFreq()
	{
		return this.freq;
	}
	
	public Node getLeft()
	{
		return this.left;
	}
	
	public Node getRight()
	{
		return this.right;
	}
	
	public char getChar()
	{
		return this.ch;
	}

}
