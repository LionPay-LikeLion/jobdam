#!/bin/bash

API_BASE="http://localhost:8081/api"
EMAIL="test@jobdam.com"
NEW_PASSWORD="newpassword123"

echo "🧪 Testing Password Reset Flow"
echo "================================"

# Step 1: Send password reset code
echo "📧 Step 1: Sending password reset code to $EMAIL"
RESPONSE1=$(curl -s -X POST "$API_BASE/auth/password-reset/send-code" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\"}")

echo "Response: $RESPONSE1"

if [[ "$RESPONSE1" == *"발송되었습니다"* ]]; then
    echo "✅ Step 1 SUCCESS: Code sent"
else
    echo "❌ Step 1 FAILED: $RESPONSE1"
    exit 1
fi

# Step 2: Check backend logs for the generated code
echo ""
echo "🔍 Step 2: Looking for verification code in backend logs..."
sleep 2

# Extract the verification code from backend logs
CODE=$(tail -50 backend.log | grep -o "비밀번호 재설정을 위한 인증코드: [0-9]\{6\}" | tail -1 | grep -o "[0-9]\{6\}")

if [ -z "$CODE" ]; then
    echo "❌ Could not find verification code in logs"
    echo "Recent log entries:"
    tail -10 backend.log
    exit 1
fi

echo "✅ Found verification code: $CODE"

# Step 3: Verify the code
echo ""
echo "🔑 Step 3: Verifying code $CODE"
RESPONSE2=$(curl -s -X POST "$API_BASE/auth/password-reset/verify-code" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"code\":\"$CODE\"}")

echo "Response: $RESPONSE2"

if [[ "$RESPONSE2" == "true" ]]; then
    echo "✅ Step 3 SUCCESS: Code verified"
else
    echo "❌ Step 3 FAILED: Code verification failed"
    exit 1
fi

# Step 4: Set new password
echo ""
echo "🔒 Step 4: Setting new password"
RESPONSE3=$(curl -s -X POST "$API_BASE/auth/password-reset/set-new-password" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"code\":\"$CODE\",\"newPassword\":\"$NEW_PASSWORD\"}")

echo "Response: $RESPONSE3"

if [[ "$RESPONSE3" == *"성공적으로"* ]]; then
    echo "✅ Step 4 SUCCESS: Password reset"
else
    echo "❌ Step 4 FAILED: $RESPONSE3"
    exit 1
fi

# Step 5: Test login with new password
echo ""
echo "🚪 Step 5: Testing login with new password"
RESPONSE4=$(curl -s -X POST "$API_BASE/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"password\":\"$NEW_PASSWORD\"}")

echo "Response: $RESPONSE4"

if [[ "$RESPONSE4" == *"accessToken"* ]]; then
    echo "✅ Step 5 SUCCESS: Login with new password works!"
    echo ""
    echo "🎉 ALL TESTS PASSED! Password reset flow is working correctly."
else
    echo "❌ Step 5 FAILED: Cannot login with new password"
    echo "This confirms the password was not actually updated in the database"
    exit 1
fi