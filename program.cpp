#include <iostream> 
#include <cmath>

using namespace std;

int main ( ) {
	int a , b , c;
	float d , x1 , x2;
	string s1 = 'корней нет';
	string s2 = 'введите a,b,c';
	cout << s2 << "\n";
	cin >> a >> b >> c;
	d = pow ( b ,2) - 4 * a * c;
	if ( d > 0 ) {
	x1 = ( sqrt ( d ) - b ) / ( 2 * a );
	x2 = ( sqrt ( d ) + b ) / ( 2 * a );
	x2 -= x2 * 2;
	cout << x1 << x2 << "\n";
	}
	else {
	if ( d == 0 ) {
	x1 = b / ( 2 * a );
	x1 -= x1 * 2;
	cout << x1 << "\n";
	}
	else {
	cout << s1 << "\n";
	}
	}
	}
	