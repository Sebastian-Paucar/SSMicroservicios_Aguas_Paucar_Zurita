export const environment  ={
  production: false,
  endpointUsers: "http://localhost:8001/",
  endpointCourses: "http://localhost:8002/",

  authorize_uri: 'http://localhost:9000/oauth2/authorize?',
  client_id : 'client',
  redirect_uri: 'http://127.0.0.1:4200/login',
  scope: 'openid',
  response_type: 'code',
  response_mode: 'form_post',
  code_challenge_method: 'S256',
  code_challenge: 'X2-y3umkMNmYPEXYW3gXf04tLAgWrC-3ErorM8TVNlE',
  code_verifier: 'z6RNVHB0H3Og94goMunateJutjbKNOvAdlSBPniJFYv',
  token_url:'http://localhost:9000/oauth2/token',
  grant_type:'authorization_code',
  logout_url:'http://localhost:9000/perform_logout',
  login_url:'http://localhost:4200/login',

}

