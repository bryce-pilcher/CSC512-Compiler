#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)

int main() {
    int a, sum;
    a=1+2+3+4+5;
    read(a);
    sum = (a+1) *a / 2;
    write(sum);
}
