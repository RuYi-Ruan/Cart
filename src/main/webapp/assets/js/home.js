// 定义一个数组用于存储购物车中的商品及数量
const cartItems = [];
// 定义一个对象，用于保存当前选中的商品信息
let selectedProduct = { id: '', name: '', quantity: 1, salePrice: null };

// 监听分页按钮点击事件
document.addEventListener('click', function (event) {
    // 如果点击了具体页码按钮
    if (event.target.classList.contains('pagination-link')) {
        event.preventDefault();
        const page = parseInt(event.target.getAttribute('data-page'));
        loadGoodsByPage(page);             // 加载对应页码的商品内容
        updatePaginationActiveState(page);  // 更新分页按钮的激活状态
    }

    // 如果点击了“上一页”按钮
    if (event.target.getAttribute('aria-label') === 'Previous') {
        event.preventDefault();
        const currentPage = parseInt(document.querySelector('.pagination .active .page-link').getAttribute('data-page'));
        if (currentPage > 1) {
            loadGoodsByPage(currentPage - 1);  // 加载前一页内容
            updatePaginationActiveState(currentPage - 1);  // 更新激活状态
        }
    }

    // 如果点击了“下一页”按钮
    if (event.target.getAttribute('aria-label') === 'Next') {
        event.preventDefault();
        const currentPage = parseInt(document.querySelector('.pagination .active .page-link').getAttribute('data-page'));
        const totalPages = parseInt(document.querySelector('.pagination').getAttribute('data-total-pages'));
        if (currentPage <= totalPages) {
            loadGoodsByPage(currentPage + 1);  // 加载下一页内容
            updatePaginationActiveState(currentPage + 1);  // 更新激活状态
        }
    }
});

// 加载指定页码的商品内容
function loadGoodsByPage(page) {
    fetch(`UserHome?page=${page}`, {
        method: 'GET',
        headers: { 'X-Requested-With': 'XMLHttpRequest' }  // AJAX 请求头
    })
        .then(response => response.text())
        .then(html => {
            document.getElementById('goodsContainer').innerHTML = html;  // 插入加载的商品内容
        })
        .catch(error => console.error('加载商品失败:', error));
}

// 更新分页按钮的激活状态
function updatePaginationActiveState(page) {
    const paginationLinks = document.querySelectorAll('.pagination-link');
    paginationLinks.forEach(link => {
        link.parentElement.classList.remove('active');
        if (parseInt(link.getAttribute('data-page')) === page) {
            link.parentElement.classList.add('active');  // 设置当前页为激活状态
        }
    });

    // 获取“上一页”和“下一页”按钮的状态
    const prevButton = document.querySelector('.page-item .page-link[aria-label="Previous"]').parentElement;
    const nextButton = document.querySelector('.page-item .page-link[aria-label="Next"]').parentElement;
    const totalPages = parseInt(document.querySelector('.pagination').getAttribute('data-total-pages'));

    // 根据页码禁用/启用“上一页”和“下一页”按钮
    page <= 1 ? prevButton.classList.add('disabled') : prevButton.classList.remove('disabled');
    page >= totalPages ? nextButton.classList.add('disabled') : nextButton.classList.remove('disabled');
}

// 点击购物车按钮时保存数据
const cartModal = document.getElementById('cartModal');  // 购物车模态框
const quantityInput = document.getElementById('quantityInput');  // 商品数量输入框
const confirmButton = document.getElementById('confirmButton');  // 确认按钮
const cartLink = document.getElementById('cartLink');  // 购物车页面链接

// 显示模态框时加载商品信息
cartModal.addEventListener('show.bs.modal', function (event) {
    const button = event.relatedTarget;
    selectedProduct.id = button.getAttribute('data-id');
    selectedProduct.name = button.getAttribute('data-name');
    selectedProduct.salePrice = button.getAttribute('data-salePrice');
});

// 隐藏模态框时重置商品数量输入框
cartModal.addEventListener('hidden.bs.modal', function () {
    quantityInput.value = 1;
});

// 将商品添加到购物车
confirmButton.addEventListener('click', function () {
    selectedProduct.quantity = quantityInput.value;
    const existingItem = cartItems.find(item => item.id === selectedProduct.id);
    if (existingItem) {
        existingItem.quantity = parseInt(existingItem.quantity) + parseInt(selectedProduct.quantity);
    } else {
        cartItems.push({ ...selectedProduct });
    }
    const modal = bootstrap.Modal.getInstance(cartModal);  // 获取模态框实例
    modal.hide();  // 隐藏模态框
});

// 点击“我的购物车”链接，发送购物车数据到服务器
cartLink.addEventListener('click', function (event) {
    event.preventDefault();
    // 转换 cartItems 中的 quantity 和 salePrice 为数字
    cartItems.forEach(item => {
        item.quantity = parseInt(item.quantity, 10);    // 转换为整数
        item.salePrice = parseFloat(item.salePrice);    // 转换为浮点数
    });

    const cartData = JSON.stringify(cartItems);  // 将购物车数据转为 JSON 字符串
    console.log(cartData)

    fetch('CartServlet', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: cartData
    })
        .then(response => {
            // 检查是否 JSON 格式，如果不是，则使用 text()
            if (response.headers.get("content-type")?.includes("application/json")) {
                return response.json();
            } else {
                return response.text();
            }
        })
        .then(data => {
            console.log(data)
            if (data.ok) {
                window.location.href = 'MyCart';  // 成功则跳转到购物车处理页面
            } else {
                alert("购物车提交失败，请重试！");
            }
        })
        .catch(error => console.error('请求出错:', error));
});

// 气泡提示框
document.addEventListener("DOMContentLoaded", function () {
    const avatar = document.getElementById("avatarPopover");
    const popoverContent = document.getElementById("userPopoverContent").innerHTML;

    // 初始化 Popover
    const popover = new bootstrap.Popover(avatar, {
        content: popoverContent,
        html: true,
        placement: "bottom",
    });

    let popoverElement;

    // 显示 Popover 时的回调
    avatar.addEventListener("mouseenter", function () {
        popover.show();
        popoverElement = document.querySelector(".popover");
        if (popoverElement) {
            popoverElement.addEventListener("mouseenter", () => clearTimeout(hidePopoverTimeout));
            popoverElement.addEventListener("mouseleave", hidePopoverWithDelay);
        }
    });

    // 隐藏 Popover 时的回调
    avatar.addEventListener("mouseleave", hidePopoverWithDelay);

    let hidePopoverTimeout;

    // 延时200ms隐藏
    function hidePopoverWithDelay() {
        hidePopoverTimeout = setTimeout(() => {
            if (popoverElement) {
                popover.hide();
            }
        }, 200);
    }
});
