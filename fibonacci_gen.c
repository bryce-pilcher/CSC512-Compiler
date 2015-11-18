#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)
#define print(x) printf(x)
int global[16];
void initialize_array(void){
int local[3];
local[1] = 16;
local[0] = 0;
c0:;
if(local[0]<local[1]) goto c1;
goto c2;
c1:;
global[local[0]] =  - 1;
local[2] = local[0] + 1;
local[0] = local[2];
goto c0;
c2:;
}
int fib(int val){
int local[6];
local[0] = val;
if(local[0]<2) goto c3;
goto c4;
c3:;
local[1] = 1;
return  local[1];
c4:;
if(global[local[0]]== - 1) goto c5;
goto c6;
c5:;
local[2] = local[0] - 1;
local[1] = fib(local[2]);
local[5] = local[0] - 2;
local[4] = fib(local[5]);
local[3] = local[1] + local[4];
global[local[0]] = local[3];
c6:;
return global[local[0]];
}
int main(void){
int local[4];
local[1] = 16;
initialize_array();
local[0] = 0;
print("The first few digits of the Fibonacci sequence are:\n");
c7:;
if(local[0]<local[1]) goto c8;
goto c9;
c8:;
local[2] = fib(local[0]);
write(local[2]);
local[3] = local[0] + 1;
local[0] = local[3];
goto c7;
c9:;
}
