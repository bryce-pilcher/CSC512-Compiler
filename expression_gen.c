#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)
int main(){
int local[9];
local[2] = 1 + 2;
local[3] = local[2] + 3;
local[4] = local[3] + 4;
local[5] = local[4] + 5;
local[0] = local[5];
read(local[0]);
local[6] = local[0] + 1;
local[7] = local[6] * local[0];
local[8] = local[7] / 2;
local[1] = local[8];
write(local[1]);
}
