#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)
#define print(x) printf(x)
void recursedigit(int n){
int local[10];
local[1] = n;
if(0==local[1]) goto c0;
goto c1;
c0:;
local[3] = 2 / 3;
local[5] = 4 * 5;
local[2] = 1 + local[3];
local[4] = local[2] - local[5];
local[0] = local[4];
return;
c1:;
local[0] = 0;
local[7] = local[1] / 2;
local[8] = local[7] * 2;
local[6] = local[1] - local[8];
if(0!=local[6]) goto c2;
goto c3;
c2:;
local[0] = 1;
c3:;
local[9] = local[1] / 2;
recursedigit(local[9]);
if(0==local[0]) goto c4;
goto c5;
c4:;
print("0");
c5:;
if(1==local[0]) goto c6;
goto c7;
c6:;
print("1");
c7:;
}
int main(){
int local[1];
local[0] = 0;
c8:;
if(0>=local[0]) goto c9;
goto c10;
c9:;
print("Give me a number: ");
read(local[0]);
if(0>=local[0]) goto c11;
goto c12;
c11:;
print("I need a positive integer.\n");
c12:;
goto c8;
c10:;
print("The binary representation of: ");
write(local[0]);
print("is: ");
recursedigit(local[0]);
print("\n\n");
}
