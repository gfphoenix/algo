package com.fphoenix.common;

import java.util.Arrays;

public class BitMap {
	private byte[] bits;

	public BitMap(int nr) {
		nr = ((nr + 7) / 8); // bytes
		bits = new byte[nr];
	}

	public BitMap(byte[] bits) {
		this.bits = bits;
		Arrays.fill(bits, (byte) 0);
	}
	public int getBitsNumber(){
		return bits.length*8;
	}

	public void clearAll(){
		Arrays.fill(bits, (byte) 0);
	}
	public void set(int nr) {
		assert nr >= 0 && nr < this.bits.length * 8;

		int off = nr / 8;
		int remain = nr % 8;
		this.bits[off] |= (1 << remain);
	}

	public void clear(int nr) {
		assert nr >= 0 && nr < this.bits.length * 8;

		int off = nr / 8;
		int remain = nr % 8;
		this.bits[off] &= ~(1 << remain);
	}

	public boolean isSet(int nr) {
		assert nr >= 0 && nr < this.bits.length * 8;

		int off = nr / 8;
		int remain = nr % 8;
		return ((this.bits[off] >> remain) & 1) == 1;
	}

	public void toggle(int nr) {
		assert nr >=0 && nr<this.bits.length*8;
		int off = nr/8;
		int remain = nr % 8;
		this.bits[off] ^= (1 << remain);
	}

	private int findFirstZero(byte b) {
		if (b == 0xff)
			return -1;
		int i = 0;
		while ((b & 0x01) == 0) {
			b >>= 1;
			i++;
		}
		return i;
	}

	//
	/**
	 * find first zero bit in bitmap
	 * 
	 * @return >=0 is ok, -1 means no zero
	 */
	public int findFirstZero() {
		int i = 0;
		for (byte b : bits) {
			if (b != 255) {
				return i + findFirstZero(b);
			}
			i += 8;
		}
		return -1;
	}
	public String dump(){
		int n = getBitsNumber();
		char []ch = new char [n];
		for(int i=0; i<n; i++){
			if(isSet(i))
				ch[i]='1';
			else
				ch[i]='0';
		}
		return new String(ch);
	}
}
