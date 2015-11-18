#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)
int global[1];
int main(){
int local[4];
read(local[0]);
read(local[1]);
read(global[0]);
if(local[0]>=local[1]) goto c0;
goto c1;
c0:;
if(local[0]>=global[0]) goto c2;
goto c3;
c2:;
write(local[0]);
c3:;
if(global[0]>local[0]) goto c4;
goto c5;
c4:;
write(global[0]);
c5:;
c1:;
local[2] = local[1]>local[0];
local[3] = local[1]>global[0];
if(local[2] && local[3]) goto c6;
goto c7;
c6:;
write(local[1]);
c7:;
if(global[0]>=local[1]) goto c8;
goto c9;
c8:;
write(global[0]);
c9:;
}
