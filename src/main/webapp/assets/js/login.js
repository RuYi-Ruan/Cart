
document.addEventListener('DOMContentLoaded', function () {
    const loginForm = document.getElementById('loginForm');
    const accountInput = document.getElementById('account');
    const passwordInput = document.getElementById('password');
    const accountFeedback = document.getElementById('accountFeedback');
    const passwordFeedback = document.getElementById('passwordFeedback');

    // 定义用户名和密码的校验函数
    function validateAccount() {
    let isValid = true;
    if (accountInput.value.trim() === '') {
    accountFeedback.textContent = '用户名不能为空。';
    isValid = false;
} else if (/\s/.test(accountInput.value)) {
    accountFeedback.textContent = '用户名不能包含空格。';
    isValid = false;
} else if (accountInput.value.length > 11) {
    accountFeedback.textContent = '用户名长度不能超过11位。';
    isValid = false;
}

    accountInput.classList.toggle('is-invalid', !isValid);
    accountInput.classList.toggle('is-valid', isValid);
    return isValid;
}

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

    // 失焦时触发校验
    accountInput.addEventListener('blur', validateAccount);
    passwordInput.addEventListener('blur', validatePassword);

    // 输入时清除错误信息
    accountInput.addEventListener('input', function () {
    if (accountInput.classList.contains('is-invalid') && validateAccount()) {
    accountInput.classList.remove('is-invalid');
}
});

    passwordInput.addEventListener('input', function () {
    if (passwordInput.classList.contains('is-invalid') && validatePassword()) {
    passwordInput.classList.remove('is-invalid');
}
});

    // 表单提交时触发校验
    loginForm.addEventListener('submit', function (event) {
    if (!validateAccount() || !validatePassword()) {
    event.preventDefault();
}
});

    // Toast 显示逻辑
    const msg = document.getElementById('msg').textContent.trim();
    const title = document.getElementById('msg_title');
    console.log(msg)
    if (msg) {
        console.log(msg)
        const toastElement = document.getElementById('Toast');
        const toast = new bootstrap.Toast(toastElement);
        if (msg === '注册成功') {
            title.textContent = 'Success';
            title.classList.remove('text-danger');
            title.classList.add('text-success');
        } else {
            title.textContent = 'Error';
            title.classList.remove('text-success');
            title.classList.add('text-danger');
        }
        toast.show();
    }
});