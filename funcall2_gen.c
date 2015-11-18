#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)
#define print(x) printf(x)
int c(){
int local[1];
local[0] = 1;
return  local[0];
}
int b(){
int local[1];
local[0] = 2;
return  local[0];
}
int a(){
int local[1];
local[0] = 3;
return  local[0];
}
int foo(int a, int b, int c){
int local[7];
local[0] = c;
local[1] = b;
local[2] = a;
local[3] = local[2] * 3;
local[5] = local[1] * 2;
local[4] = local[3] + local[5];
local[6] = local[4] + local[0];
return local[6];
}
int main(){
int local[5];
local[2] = a();
local[3] = b();
local[4] = c();
local[1] = foo(local[2],local[3],local[4]);
local[0] = local[1];
print("I calculate the answer to be: ");
write(local[0]);
}
