#include <iostream>
#include <stdio.h>
#include <string.h>

int main()
{
    int word_1400022D0[44] =
    {
      180,
      132,
      150,
      152,
      182,
      146,
      68,
      232,
      172,
      126,
      180,
      160,
      184,
      246,
      220,
      250,
      246,
      120,
      150,
      160,
      236,
      128,
      244,
      186,
      160,
      176,
      124,
      194,
      214,
      126,
      240,
      184,
      160,
      138,
      126,
      180,
      186,
      130,
      212,
      172,
      228
    }; // idb

    for (int i = 0; i < 44; i++)
    {
        char x = word_1400022D0[i] / 2;
        x = x ^ 15;
        std::cout << x;
    }

    /*char v11[256] = { ' ' };
    char Str[256] = { 'a' };
    int v7 = 0;
    int v4 = strnlen(Str, 0x100ui64);
    int v5 = 0i64;
    int v6 = v4;
    do
    {
        v11[v7] = 2 * (Str[v7] ^ 0xF);

        //U = 2 * (85 ^ 15)
        
        ++v7;
    }       while (v7 < v6);
    int v8 = 0;
    do
    {
        if (v11[v5] != word_1400022D0[v5])
            v8 = 1;
        ++v5;
    }       while (v5 < v6);*/
}

