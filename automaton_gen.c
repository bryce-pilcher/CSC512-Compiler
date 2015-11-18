#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)
#define print(x) printf(x)
void state_0(void);
void state_1(void);
void state_2(void);
void state_3(void);
int getnextdigit(void){
int local[3];
c0:;
if(0==0) goto c1;
goto c2;
c1:;
print("Give me a number (-1 to quit): ");
read(local[0]);
local[1] =  - 1<=local[0];
local[2] = 1>=local[0];
if(local[1] && local[2]) goto c3;
goto c4;
c3:;
goto c2;
c4:;
print("I need a number that's either 0 or 1.\n");
goto c0;
c2:;
return local[0];
}
void state_0(void){
int local[2];
local[1] = getnextdigit();
local[0] = local[1];
if( - 1==local[0]) goto c5;
goto c6;
c5:;
print("You gave me an even number of 0's.\n");
print("You gave me an even number of 1's.\n");
print("I therefore accept this input.\n");
return;
c6:;
if(0==local[0]) goto c7;
goto c8;
c7:;
state_2();
c8:;
if(1==local[0]) goto c9;
goto c10;
c9:;
state_1();
c10:;
}
void state_1(void){
int local[2];
local[1] = getnextdigit();
local[0] = local[1];
if( - 1==local[0]) goto c11;
goto c12;
c11:;
print("You gave me an even number of 0's.\n");
print("You gave me an odd number of 1's.\n");
print("I therefore reject this input.\n");
return;
c12:;
if(0==local[0]) goto c13;
goto c14;
c13:;
state_3();
c14:;
if(1==local[0]) goto c15;
goto c16;
c15:;
state_0();
c16:;
}
void state_2(void){
int local[2];
local[1] = getnextdigit();
local[0] = local[1];
if( - 1==local[0]) goto c17;
goto c18;
c17:;
print("You gave me an odd number of 0's.\n");
print("You gave me an even number of 1's.\n");
print("I therefore reject this input.\n");
return;
c18:;
if(0==local[0]) goto c19;
goto c20;
c19:;
state_0();
c20:;
if(1==local[0]) goto c21;
goto c22;
c21:;
state_3();
c22:;
}
void state_3(void){
int local[2];
local[1] = getnextdigit();
local[0] = local[1];
if( - 1==local[0]) goto c23;
goto c24;
c23:;
print("You gave me an odd number of 0's.\n");
print("You gave me an odd number of 1's.\n");
print("I therefore reject this input.\n");
return;
c24:;
if(0==local[0]) goto c25;
goto c26;
c25:;
state_1();
c26:;
if(1==local[0]) goto c27;
goto c28;
c27:;
state_2();
c28:;
}
int main(){
int local[0];
state_0();
}
