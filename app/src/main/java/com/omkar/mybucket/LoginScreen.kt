package com.omkar.mybucket

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToSignUp: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onGoogleSignInClick: () -> Unit = {},
    onLoginSuccess: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val firebaseAuth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Placeholder Icon / Logo Box
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFFEFEFEF), shape = RoundedCornerShape(20.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Title & Subtitle
            Text(
                text = "Welcome Back",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E1E)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sign in to organize your tasks and conquer your day.",
                fontSize = 14.sp,
                color = Color(0xFF6E6E6E),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email Field
            Text(
                text = "Email Address",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4A4A4A),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("you@example.com", color = Color.LightGray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Icon",
                        tint = Color.Gray
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF9F9F9),
                    focusedContainerColor = Color(0xFFF9F9F9),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = Color(0xFF0047AB)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field Label Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Password",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF4A4A4A)
                )
                Text(
                    text = "Forgot?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0047AB),
                    modifier = Modifier.clickable { onForgotPasswordClick() }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("••••••••", color = Color.LightGray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock Icon",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Password Visibility",
                            tint = Color.Gray
                        )
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF9F9F9),
                    focusedContainerColor = Color(0xFFF9F9F9),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = Color(0xFF0047AB)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign In Button
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Please enter email and password"
                        return@Button
                    }
                    isLoading = true
                    errorMessage = null

                    firebaseAuth.signInWithEmailAndPassword(email.trim(), password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                onLoginSuccess()
                            } else {
                                errorMessage = task.exception?.localizedMessage ?: "Authentication failed"
                            }
                        }
                },
                enabled = !isLoading,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0047AB)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Sign In",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "➔", fontSize = 16.sp, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Divider Line
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
                Text(
                    text = "OR CONTINUE WITH",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Google Sign In Button
            OutlinedButton(
                onClick = { onGoogleSignInClick() },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFF9F9F9)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(10.dp))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Replace with your Google drawable asset: R.drawable.ic_google
                    // Image(painter = painterResource(id = R.drawable.ic_google), contentDescription = "Google")
                    Text(text = "G ", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFFEA4335))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sign in with Google",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF333333)
                    )
                }
            }
        }

        // Bottom Navigation Prompt
        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Don't have an account? ", color = Color.Gray, fontSize = 14.sp)
            Text(
                text = "Sign up",
                color = Color(0xFF0047AB),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onNavigateToSignUp() }
            )
        }
    }
}