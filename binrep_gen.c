#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)
#define print(x) printf(x)
void recursedigit(int n){
int local[6];
local[1] = n;
if(0==local[1]) goto c0;
goto c1;
c0:;
return ;
c1:;
local[0] = 0;
local[3] = local[1] / 2;
local[4] = local[3] * 2;
local[2] = local[1] - local[4];
if(0!=local[2]) goto c2;
goto c3;
c2:;
local[0] = 1;
c3:;
local[5] = local[1] / 2;
recursedigit(local[5]);
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
