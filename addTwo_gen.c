#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)
int add(int a){
int local[3];
local[1] = a;
read(local[0]);
local[2] = local[1] + local[0];
return local[2];
}
int main(){
int local[13];
read(local[0]);
local[6] = 4 + 5;
local[4] = 2 * 3;
local[5] = local[4] * local[6];
local[7] = local[5] * 6;
local[3] = 1 + local[7];
local[2] = local[3];
local[9] = 1 * 2;
local[10] = local[9] * 3;
local[8] = local[10];
local[11] = add(local[0]);
write(local[11]);
local[12] = local[2] + local[8];
write(local[12]);
}
