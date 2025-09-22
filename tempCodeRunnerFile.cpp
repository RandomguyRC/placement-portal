#include <bits/stdc++.h>
using namespace std;
#define ll long long
int main()
{
    ll t;
    cin >> t;
    while(t--)
    {
        ll n;
        cin >> n;
        ll x;
        vector<ll> v;
        for (ll i=0; i<n; i++)
        {
            cin >> x;
            v.push_back(x);
        }
        ll maxi=INT_MIN;
        for (ll i=0; i<n; i++)
        {
            ll cnt=0;
            for (ll j=i; j<n; j++)
            {
                if (v[i]==v[j])
                {
                    cnt++;
                }
            }
            maxi=max(cnt,maxi);
        }
        cout << maxi << endl;
    }

}