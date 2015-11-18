#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)
#define print(x) printf(x)
int square(int x){
int local[2];
local[0] = x;
local[0] = 10;
local[1] = local[0] * local[0];
return local[1];
}
int main(void){
int local[2];
print("Give me a number: ");
read(local[0]);
print("Your number squared is: ");
local[1] = square(local[0]);
write(local[1]);
}
