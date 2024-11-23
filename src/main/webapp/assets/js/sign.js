document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form');
    const accountInput = document.getElementById('account');
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const genderInput = document.getElementById('gender');
    const accountFeedback = document.getElementById('accountFeedback');
    const passwordFeedback = document.getElementById('passwordFeedback');
    const confirmPasswordFeedback = document.getElementById('confirmPasswordFeedback');
    const genderFeedback = document.getElementById('genderFeedback');

// 账号校验
function validateAccount() {
    let isValid = true;
    if (accountInput.value.trim() === '') {
accountFeedback.textContent = '账号不能为空。';
    isValid = false;
} else if (/\s/.test(accountInput.value)) {
accountFeedback.textContent = '账号不能包含空格。';
    isValid = false;
} else if (accountInput.value.length > 11) {
accountFeedback.textContent = '账号长度不能超过11位。';
    isValid = false;
}

accountInput.classList.toggle('is-invalid', !isValid);
accountInput.classList.toggle('is-valid', isValid);
    return isValid;
}

// 密码校验
function validatePassword() {
    let isValid = true;
    if (passwordInput.value.trim() === '') {
passwordFeedback.textContent = '密码不能为空。';
    isValid = false;
} else if (/\s/.test(passwordInput.value)) {
passwordFeedback.textContent = '密码不能包含空格。';
    isValid = false;
} else if (passwordInput.value.length < 6) {
passwordFeedback.textContent = '密码长度不能少于6位。';
    isValid = false;
} else if (passwordInput.value.length > 11) {
passwordFeedback.textContent = '密码长度不能超过11位。';
    isValid = false;
}

passwordInput.classList.toggle('is-invalid', !isValid);
passwordInput.classList.toggle('is-valid', isValid);
    return isValid;
}

// 确认密码校验
function validateConfirmPassword() {
    const isValid = confirmPasswordInput.value === passwordInput.value;
confirmPasswordInput.classList.toggle('is-invalid', !isValid);
confirmPasswordInput.classList.toggle('is-valid', isValid);
    return isValid;
}

// 性别校验
function validateGender() {
    const isValid = genderInput.value !== '';
genderInput.classList.toggle('is-invalid', !isValid);
genderInput.classList.toggle('is-valid', isValid);
    return isValid;
}

// 失焦事件触发校验
accountInput.addEventListener('blur', validateAccount);
passwordInput.addEventListener('blur', validatePassword);
confirmPasswordInput.addEventListener('blur', validateConfirmPassword);
genderInput.addEventListener('blur', validateGender);

// 输入时清除错误提示
accountInput.addEventListener('input', () => {
    if (accountInput.classList.contains('is-invalid') && validateAccount()) {
accountInput.classList.remove('is-invalid');
}
});

passwordInput.addEventListener('input', () => {
    if (passwordInput.classList.contains('is-invalid') && validatePassword()) {
passwordInput.classList.remove('is-invalid');
}
});

confirmPasswordInput.addEventListener('input', () => {
    if (confirmPasswordInput.classList.contains('is-invalid') && validateConfirmPassword()) {
confirmPasswordInput.classList.remove('is-invalid');
}
});

genderInput.addEventListener('input', () => {
    if (genderInput.classList.contains('is-invalid') && validateGender()) {
genderInput.classList.remove('is-invalid');
}
});

// 表单提交时触发所有校验
form.addEventListener('submit', function(event) {
    const isAccountValid = validateAccount();
    const isPasswordValid = validatePassword();
    const isConfirmPasswordValid = validateConfirmPassword();
    const isGenderValid = validateGender();

    if (!isAccountValid || !isPasswordValid || !isConfirmPasswordValid || !isGenderValid) {
event.preventDefault();
}
});
});