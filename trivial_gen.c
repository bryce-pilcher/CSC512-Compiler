#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)
void foo(){
int local[1];
read(local[0]);
write(local[0]);
}
int main(){
int local[0];
foo();
}
