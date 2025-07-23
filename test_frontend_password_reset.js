// Test script to simulate the password reset flow
const testPasswordReset = async () => {
  const baseURL = 'http://localhost:8081/api';
  const email = 'test@jobdam.com';
  
  console.log('🧪 Testing Password Reset Frontend Flow');
  console.log('======================================');
  
  // Step 1: Send password reset code
  console.log('📧 Step 1: Sending password reset code...');
  const response1 = await fetch(`${baseURL}/auth/password-reset/send-code`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email })
  });
  
  const result1 = await response1.text();
  console.log('Response:', result1);
  
  if (!result1.includes('발송되었습니다')) {
    console.log('❌ Step 1 failed');
    return;
  }
  
  console.log('✅ Step 1 SUCCESS');
  
  // For now, let's use a mock code since we can't easily extract from logs
  const code = '123456'; // This would normally come from email/logs
  
  // Test the frontend API endpoint directly
  console.log('\n🔧 Testing Frontend API Configuration...');
  console.log('Frontend should be running on: http://localhost:5173');
  console.log('Backend API URL: http://localhost:8081/api');
  
  // Test a simple frontend page fetch
  try {
    const frontendResponse = await fetch('http://localhost:5173/');
    console.log('✅ Frontend is accessible');
  } catch (error) {
    console.log('❌ Frontend not accessible:', error.message);
  }
  
  console.log('\n📝 Manual Testing Instructions:');
  console.log('1. Open browser to: http://localhost:5173/find-password');
  console.log('2. Enter email: test@jobdam.com');
  console.log('3. Check backend logs for the verification code');
  console.log('4. Enter the verification code');
  console.log('5. Set new password: newpassword456');
  console.log('6. Click the "HTML 비밀번호 재설정" button');
  console.log('7. Check browser console for debugging logs');
};

// For Node.js environment
if (typeof window === 'undefined') {
  const fetch = require('node-fetch');
  testPasswordReset().catch(console.error);
}