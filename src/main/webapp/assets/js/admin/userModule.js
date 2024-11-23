// 用户模块
const UserModule = {
    selectUrl: "select/users",
    deleteUrl: "delete/user",
    addUrl: "add/user",
    updateUrl: "update/user",
    tableId: "user-table",
    currentPage: 1,
    pageSize: 5,
    cachedData: null,
    keyword: null,

    loadData() {
        this._toggleTable();
        fetch(`${this.selectUrl}?page=${this.currentPage}&pageSize=${this.pageSize}&keyword=${this.keyword}`)
            .then(response => response.json())
            .then(result => {
                const data = result.data;
                const totalItems = result.totalItems;
                this.cachedData = data;
                const tbody = document.querySelector(`#${this.tableId} tbody`);
                tbody.innerHTML = "";

                data.forEach(item => {
                    let row = `<tr>
                    <td>${item.card_no}</td>
                    <td>${item.username}</td>
                    <td>${item.sex}</td>
                    <td>${item.age}</td>
                    <td>${item.password}</td>
                    <td>
                      <button class="btn btn-warning btn-sm edit-btn" data-id="${item.card_no}">修改</button>
                      <button class="btn btn-danger btn-sm delete-btn"
                              data-id="${item.card_no}" data-bs-toggle="popover"
                              title="确认删除？"
                              data-bs-content=" 
                              <button class='btn btn-sm btn-secondary cancel-btn'>取消</button>
                              <button class='btn btn-sm btn-danger confirm-delete-btn'>确认</button>"
                      >删除</button>
                    </td>
                  </tr>`;
                    tbody.innerHTML += row;
                });
                _removeEventListeners(this.tableId); // 移除已存在的事件监听器
                this.bindEvents();

                updatePagination(totalItems, this.currentPage, this.pageSize, (page) => {
                    this.currentPage = page;
                    this.loadData();
                });

                initPopover();
            })
            .catch(error => console.error("Error loading user data:", error));
    },

    _toggleTable() {
        document.getElementById('home-card').classList.add('d-none');
        document.getElementById('operate-area').classList.remove('d-none');
        document.getElementById('user-table').classList.remove('d-none');
        document.getElementById('goods-table').classList.add('d-none');
        document.getElementById('salebill-table').classList.add('d-none');
        document.getElementById('pagination-nav').classList.remove('d-none');
    },

    bindEvents() {
        // 修改按钮绑定事件
        document.querySelectorAll(`#${this.tableId} .edit-btn`).forEach(button => {
            button.addEventListener("click", (event) => {
                const id = event.target.getAttribute('data-id');
                this.openOffcanvas("修改用户", id);
            });
        });

        // 删除按钮绑定事件
        document.querySelectorAll(`#${this.tableId} .delete-btn`).forEach(button => {
            button.addEventListener("click", event => {
                const popoverInstance = new bootstrap.Popover(button, {
                    html: true,
                    sanitize: false
                });

                button.addEventListener('shown.bs.popover', () => {
                    const popoverContent = document.querySelector('.popover-body');

                    popoverContent.querySelector(".cancel-btn")?.addEventListener("click", () => {
                        popoverInstance.hide();
                    });

                    popoverContent.querySelector(".confirm-delete-btn")?.addEventListener("click", () => {
                        const id = event.target.getAttribute('data-id');
                        this.handleDelete(id, button);
                        popoverInstance.hide();
                    });
                });

                popoverInstance.show();
            });
        });

        // 添加用户按钮绑定事件
        document.getElementById("add-record-button").addEventListener("click", () => {
            this.openOffcanvas("添加用户");
        });

        // 绑定事件监听
        const searchButton = document.getElementById("search-button");
        searchButton.addEventListener("click", (event) => _searchButtonClickHandler(event, UserModule));

        const resetButton = document.getElementById("reset-button");
        resetButton.addEventListener("click", (event) => _resetButtonClickHandler(event, UserModule));

    },

    openOffcanvas(title, id = null) {
        const offcanvasElement = document.getElementById("offcanvasForm");
        offcanvasElement.querySelector('.offcanvas-title').innerHTML = title;
        const isAdding = (title === "添加用户");

        offcanvasElement.querySelector("#offcanvas-form").innerHTML = `
          <div class="mb-3" id="card_no_container">
            <label for="card_no" class="form-label">卡号</label>
            <input type="text" class="form-control" id="card_no" name="card_no">
          </div>   
          <div class="mb-3">
            <label for="username" class="form-label">用户名</label>
            <input type="text" class="form-control" id="username" name="username">
          </div>
          <div class="mb-3">
            <label for="sex" class="form-label">性别</label>
            <select class="form-select" id="sex" name="sex">
              <option value="男">男</option>
              <option value="女">女</option>
            </select>
          </div>
          <div class="mb-3">
            <label for="age" class="form-label">年龄</label>
            <input type="number" class="form-control" id="age" name="age">
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">密码</label>
            <input type="text" class="form-control" id="password" name="password">
          </div>
    `;

        // 手动移除现有的 Offcanvas 遮罩层
        document.querySelectorAll('.offcanvas-backdrop').forEach(backdrop => backdrop.remove());

        const offcanvas = new bootstrap.Offcanvas(offcanvasElement);
        offcanvas.show();

        if (id) {
            this.loadUserData(id); // 如果是编辑模式，加载用户数据
            // 设置 card_no 为只读
            document.getElementById("card_no").readOnly = true;
        } else {
            // 新增模式下取消只读限制
            document.getElementById("card_no").readOnly = false;
        }

        // 清除 submitButton 的所有事件监听器
        const submitButton = document.getElementById("submitFormButton");
        const newSubmitButton = submitButton.cloneNode(true); // 克隆一个新的按钮
        submitButton.parentNode.replaceChild(newSubmitButton, submitButton);

        // 为新按钮添加事件监听器
        newSubmitButton.addEventListener("click", () => {
            this.saveUserData(id);
            offcanvas.hide();
        });

        // 监听 Offcanvas 关闭事件以确保移除遮罩层
        offcanvasElement.addEventListener('hidden.bs.offcanvas', () => {
            document.querySelectorAll('.offcanvas-backdrop').forEach(backdrop => backdrop.remove());
        });
    },

    loadUserData(id) {
        const user = this.cachedData.find(item => item.card_no === id);

        if (user) {
            document.getElementById('card_no').value = user.card_no;
            document.getElementById("username").value = user.username;
            document.getElementById("sex").value = user.sex;
            document.getElementById("age").value = user.age;
            document.getElementById("password").value = user.password
        } else {
            showToast("加载失败", "未找到用户信息，请重试。", false);
        }
    },

    saveUserData(id) {
        const form = document.getElementById("offcanvas-form");
        const formData = new FormData(form);
        const data = Object.fromEntries(formData.entries());

        console.log(data)
        const url = id ? this.updateUrl : this.addUrl;

        fetch(url, {
            method: "post",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showToast("成功", data.message, true);
                    this.loadData();
                } else {
                    showToast("失败", data.message, false);
                }
            })
            .catch(error => {
                console.error("Error saving user data:", error);
                showToast("保存失败", "发生网络错误，请稍后再试。", false);
            });
    },

    handleDelete(id, button) {
        fetch(`${this.deleteUrl}?id=${id}`, { method: "DELETE" })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showToast("删除成功", data.message, true);
                    this.loadData();
                } else {
                    showToast("删除失败", data.message, false);
                }
            })
            .catch(error => {
                console.error("Error deleting user:", error);
                showToast("删除失败", "发生网络错误，请稍后再试。", false);
            });
    }
};

document.getElementById("user-info").addEventListener("click", () => UserModule.loadData());
