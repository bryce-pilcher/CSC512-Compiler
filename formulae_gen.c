#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)
#define print(x) printf(x)
int getinput(void){
int local[1];
local[0] = 0;
c0:;
if(0>=local[0]) goto c1;
goto c2;
c1:;
read(local[0]);
if(0>local[0]) goto c3;
goto c4;
c3:;
print("I need a positive number: ");
c4:;
goto c0;
c2:;
return local[0];
}
int main(){
int local[34];
local[10] = 3141;
print("Give me a radius for the base of a cone: ");
local[11] = getinput();
local[0] = local[11];
print("Give me a height for a cone: ");
local[12] = getinput();
local[1] = local[12];
print("Give me a radius for a circle: ");
local[13] = getinput();
local[2] = local[13];
print("Give me a length for the base of a triangle: ");
local[14] = getinput();
local[3] = local[14];
print("Give me a height for a triangle: ");
local[15] = getinput();
local[4] = local[15];
print("Give me a radius for a sphere: ");
local[16] = getinput();
local[5] = local[16];
local[17] = local[10] * local[0];
local[18] = local[17] * local[0];
local[19] = local[18] * local[1];
local[20] = local[19] + 500;
local[21] = local[20] / 3000;
local[6] = local[21];
local[22] = local[10] * local[2];
local[23] = local[22] * local[2];
local[24] = local[23] + 500;
local[25] = local[24] / 1000;
local[7] = local[25];
local[26] = local[3] * local[4];
local[27] = local[26] / 2;
local[8] = local[27];
local[28] = 4 * local[10];
local[29] = local[28] * local[5];
local[30] = local[29] * local[5];
local[31] = local[30] * local[5];
local[32] = local[31] + 500;
local[33] = local[32] / 3000;
local[9] = local[33];
print("The volume of the cone is: ");
write(local[6]);
print("The area of the circle is: ");
write(local[7]);
print("The area of the triangle is: ");
write(local[8]);
print("The volume of the sphere is: ");
write(local[9]);
}
