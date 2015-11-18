#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)
int recursionsum(int n){
int local[5];
local[0] = n;
if(local[0]==0) goto c0;
goto c1;
c0:;
local[1] = 0;
return  local[1];
c1:;
local[4] = local[0] - 1;
local[3] = recursionsum(local[4]);
local[2] = local[0] + local[3];
return local[2];
}
int main(){
int local[2];
read(local[0]);
local[1] = recursionsum(local[0]);
write(local[1]);
}
