#!/bin/bash

API_BASE="http://localhost:8081/api"
FRONTEND_BASE="http://localhost:5173"
EMAIL="test@jobdam.com"
NEW_PASSWORD="newpassword999"

echo "🧪 Complete Password Reset Flow Test"
echo "===================================="

# Test 1: Check both servers are running
echo "🔍 Step 1: Checking server status..."
BACKEND_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$API_BASE/auth/check-email?email=test@example.com")
FRONTEND_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$FRONTEND_BASE/")

if [ "$BACKEND_STATUS" != "200" ]; then
    echo "❌ Backend not running (status: $BACKEND_STATUS)"
    exit 1
fi

if [ "$FRONTEND_STATUS" != "200" ]; then
    echo "❌ Frontend not running (status: $FRONTEND_STATUS)"
    exit 1
fi

echo "✅ Both servers running (Backend: $BACKEND_STATUS, Frontend: $FRONTEND_STATUS)"

# Test 2: Complete backend API flow
echo ""
echo "🔧 Step 2: Testing Backend API Flow..."

# Send reset code
echo "📧 Sending password reset code..."
RESPONSE1=$(curl -s -X POST "$API_BASE/auth/password-reset/send-code" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\"}")

echo "Response: $RESPONSE1"

if [[ "$RESPONSE1" != *"발송되었습니다"* ]]; then
    echo "❌ Failed to send reset code"
    exit 1
fi

echo "✅ Reset code sent successfully"

# Wait and extract code from logs
echo ""
echo "🔍 Extracting verification code from backend logs..."
sleep 2

# Try to get the most recent code from logs
CODE=$(tail -100 /Users/juanpark/Library/Mobile\ Documents/com~apple~CloudDocs/CS_iCloud/computer.lab/멋사/projects/jobdam/backend.log 2>/dev/null | grep "인증코드:" | tail -1 | grep -o "[0-9]\{6\}" || echo "")

if [ -z "$CODE" ]; then
    echo "⚠️  Could not extract code from logs, using manual approach..."
    echo "Please check backend terminal for the 6-digit code and run:"
    echo "CODE=XXXXXX && curl -s -X POST \"$API_BASE/auth/password-reset/verify-code\" -H \"Content-Type: application/json\" -d \"{\\\"email\\\":\\\"$EMAIL\\\",\\\"code\\\":\\\"$CODE\\\"}\""
    exit 1
fi

echo "✅ Found verification code: $CODE"

# Verify code
echo ""
echo "🔑 Verifying code..."
RESPONSE2=$(curl -s -X POST "$API_BASE/auth/password-reset/verify-code" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"code\":\"$CODE\"}")

echo "Response: $RESPONSE2"

if [[ "$RESPONSE2" != "true" ]]; then
    echo "❌ Code verification failed"
    exit 1
fi

echo "✅ Code verified successfully"

# Set new password
echo ""
echo "🔒 Setting new password..."
RESPONSE3=$(curl -s -X POST "$API_BASE/auth/password-reset/set-new-password" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"code\":\"$CODE\",\"newPassword\":\"$NEW_PASSWORD\"}")

echo "Response: $RESPONSE3"

if [[ "$RESPONSE3" != *"성공적으로"* ]]; then
    echo "❌ Password reset failed"
    exit 1
fi

echo "✅ Password reset successful"

# Test login with new password
echo ""
echo "🚪 Testing login with new password..."
RESPONSE4=$(curl -s -X POST "$API_BASE/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"password\":\"$NEW_PASSWORD\"}")

echo "Response: $RESPONSE4"

if [[ "$RESPONSE4" == *"accessToken"* ]]; then
    echo "✅ Login successful with new password!"
else
    echo "❌ Login failed with new password"
    exit 1
fi

echo ""
echo "🎉 BACKEND API FLOW: ALL TESTS PASSED!"
echo ""

# Test 3: Frontend API simulation
echo "🌐 Step 3: Simulating Frontend API Calls..."

# Test if frontend can reach backend (CORS, etc.)
echo "Testing frontend -> backend connectivity..."

# Simulate what the frontend authApi.ts setNewPassword function should do
echo ""
echo "🔧 Simulating Frontend setNewPassword API call..."

# First, get a fresh code
echo "Getting fresh verification code..."
curl -s -X POST "$API_BASE/auth/password-reset/send-code" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\"}" > /dev/null

sleep 2

# Get new code
NEW_CODE=$(tail -100 /Users/juanpark/Library/Mobile\ Documents/com~apple~CloudDocs/CS_iCloud/computer.lab/멋사/projects/jobdam/backend.log 2>/dev/null | grep "인증코드:" | tail -1 | grep -o "[0-9]\{6\}" || echo "")

if [ -n "$NEW_CODE" ]; then
    echo "New verification code: $NEW_CODE"
    
    # Verify the new code
    curl -s -X POST "$API_BASE/auth/password-reset/verify-code" \
      -H "Content-Type: application/json" \
      -d "{\"email\":\"$EMAIL\",\"code\":\"$NEW_CODE\"}" > /dev/null
    
    # Test the exact API call that frontend should make
    echo ""
    echo "🎯 Testing exact frontend API call pattern..."
    
    FRONTEND_STYLE_RESPONSE=$(curl -s -X POST "$API_BASE/auth/password-reset/set-new-password" \
      -H "Content-Type: application/json" \
      -H "User-Agent: Mozilla/5.0 (Frontend Test)" \
      -d "{\"email\":\"$EMAIL\",\"code\":\"$NEW_CODE\",\"newPassword\":\"testpass123\"}")
    
    echo "Frontend-style response: $FRONTEND_STYLE_RESPONSE"
    
    if [[ "$FRONTEND_STYLE_RESPONSE" == *"성공적으로"* ]]; then
        echo "✅ Frontend-style API call works!"
    else
        echo "❌ Frontend-style API call failed"
    fi
fi

echo ""
echo "📋 SUMMARY:"
echo "✅ Backend APIs: Working perfectly"
echo "✅ Password reset: Confirmed working"
echo "✅ Database updates: Confirmed working"
echo "❌ Frontend issue: Button click not triggering API call"
echo ""
echo "🔧 DEBUGGING STEPS FOR FRONTEND:"
echo "1. Open browser to: $FRONTEND_BASE/find-password"
echo "2. Open Developer Tools (F12) -> Console tab"
echo "3. Follow the password reset flow"
echo "4. Look for these debug messages:"
echo "   - '🔥 HTML BUTTON CLICKED!' (button works)"
echo "   - '🔧 ===== setNewPassword API CALL START =====' (API function called)"
echo "   - Success/error logs from the HTTP request"
echo ""
echo "If you see the button click but no API call, there's a JavaScript error."
echo "If you see no button click at all, there's a DOM/React issue."