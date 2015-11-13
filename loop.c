#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)

int main() {
    int a, sum;
    read(a);
    sum = 0;
    while (1==1) {
        if(a==2){
        	continue;
	}
        sum = sum + a;
        a = a - 1;
        if(a==0){
             break;
        }
    }
    write(sum);
}

