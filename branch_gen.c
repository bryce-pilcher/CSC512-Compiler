#include <stdio.h>
 #define read(x) scanf("%d",&x)
 #define write(x) printf("%d\n",x)
 int c;   int main() {     int a, b;     read(a);     read(b);     read(c);     if (a>=b) {  if (a>=c) {      write(a);  }  if(c>a) {      write(c);  }     }     if (b>a && b>c) {         write(b);     }     if(c>=b){         write(c);     } }   