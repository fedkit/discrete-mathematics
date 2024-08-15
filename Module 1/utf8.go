package main

import (
	"fmt"
)

func encode(u32 []rune) []byte {
	var u8 []byte
	for _, r := range u32 {
		switch {
		case r <= 0x7F:
			u8 = append(u8, byte(r))
		case r <= 0x7FF:
			u8 = append(u8, byte(0xC0|(r>>6)), byte(0x80|(r&0x3F)))
		case r <= 0xFFFF:
			u8 = append(u8, byte(0xE0|(r>>12)), byte(0x80|((r>>6)&0x3F)), byte(0x80|(r&0x3F)))
		default:
			u8 = append(u8, byte(0xF0|(r>>18)), byte(0x80|((r>>12)&0x3F)), 
			                byte(0x80|((r>>6)&0x3F)), byte(0x80|(r&0x3F)))
		}
	}
	return u8
}

func decode(u8 []byte) []rune {
	var u32 []rune
	i := 0
	for i < len(u8) {
		var r rune
		var size int
		switch {
		case u8[i]&0x80 == 0:
			r = rune(u8[i])
			size = 1
		case u8[i]&0xE0 == 0xC0:
			r = rune(u8[i]&0x1F)<<6 |
				rune(u8[i+1]&0x3F)
			size = 2
		case u8[i]&0xF0 == 0xE0:
			r = rune(u8[i]&0x0F)<<12 |
				rune(u8[i+1]&0x3F)<<6 |
				rune(u8[i+2]&0x3F)
			size = 3
		case u8[i]&0xF8 == 0xF0: 
			r = rune(u8[i]&0x07)<<18 |
				rune(u8[i+1]&0x3F)<<12 |
				rune(u8[i+2]&0x3F)<<6 |
				rune(u8[i+3]&0x3F)
			size = 4
		}
		u32 = append(u32, r)
		i += size
	}
	return u32
}

func main() {
	text := []rune{0x0414, 0x0438, 0x0441, 0x043A, 0x0440, 0x0435, 0x0442, 0x043D, 0x0430, 0x044F, 0x0020,
		0x043C, 0x0430, 0x0442, 0x0435, 0x043C, 0x0430, 0x0442, 0x0438, 0x043A, 0x0430}
	fmt.Println(encode(text))
	fmt.Println(decode(encode(text)))
}
