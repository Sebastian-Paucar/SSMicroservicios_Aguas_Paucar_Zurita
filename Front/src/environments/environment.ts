export const environment  ={
  production: false,
  endpointUsers: "https://api-gateway-fmc0.onrender.com/crud/",
  endpointFree: "https://api-gateway-fmc0.onrender.com/productos/",
  authorize_uri: 'https://securityecovida.onrender.com/oauth2/authorize?',
  client_id : 'client',
  redirect_uri: 'https://ecovida-ten.vercel.app/login',
  scope: 'openid',
  response_type: 'code',
  response_mode: 'form_post',
  code_challenge_method: 'S256',
  code_challenge: 'X2-y3umkMNmYPEXYW3gXf04tLAgWrC-3ErorM8TVNlE',
  code_verifier: 'z6RNVHB0H3Og94goMunateJutjbKNOvAdlSBPniJFYv',
  token_url:'https://securityecovida.onrender.com/oauth2/token',
  grant_type:'authorization_code',
  logout_url:'https://securityecovida.onrender.com/logout',
  login_url:'https://ecovida-ten.vercel.app/login',

}

