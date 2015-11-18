#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)
#define print(x) printf(x)
int global[8];
void populate_arrays(void){
int local[0];
global[0] = 0;
global[1] = 1;
global[2] = 1;
global[3] = 2;
global[4] = 3;
global[5] = 5;
global[6] = 8;
global[7] = 13;
}
int main(void){
int local[3];
populate_arrays();
local[0] = 0;
local[1] = 8;
print("The first few digits of the Fibonacci sequence are:\n");
c0:;
if(local[0]<local[1]) goto c1;
goto c2;
c1:;
write(global[local[0]]);
local[2] = local[0] + 1;
local[0] = local[2];
goto c0;
c2:;
}
