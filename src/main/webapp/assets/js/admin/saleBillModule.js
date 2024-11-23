// 账单模块
const SaleBillModule = {
    selectUrl: "select/salebills",
    deleteUrl: "delete/salebills",
    addUrl: "add/salebill",
    updateUrl: "update/salebill",
    tableId: "salebill-table",
    currentPage: 1,
    pageSize: 5,
    cachedData: null,
    keyword: null,

    loadData() {
        this._toggleTable();
        fetch(`${this.selectUrl}?page=${this.currentPage}&pageSize=${this.pageSize}&keyword=${this.keyword}`)
            .then(response => response.json())
            .then(result => {
                const { data, totalItems } = result;
                this.cachedData = data;

                const tbody = document.querySelector(`#${this.tableId} tbody`);
                tbody.innerHTML = data.map(item => `
                    <tr>
                      <td>${item.goods_no}</td>
                      <td>${item.card_no}</td>
                      <td>${item.number}</td>
                      <td>${item.total_price}</td>
                      <td>${item.bill_date}</td>
                      <td>
                        <button class="btn btn-warning btn-sm edit-btn" data-id="${item.goods_no}">修改</button>
                        <button class="btn btn-danger btn-sm delete-btn" data-id="${item.goods_no}" 
                                data-bs-toggle="popover" title="确认删除？"
                                data-bs-content="
                                <button class='btn btn-sm btn-secondary cancel-btn'>取消</button>
                                <button class='btn btn-sm btn-danger confirm-delete-btn'>确认</button>">删除</button>
                      </td>
                    </tr>
                `).join('');

                _removeEventListeners();
                this.bindEvents();

                // 更新分页器
                updatePagination(totalItems, this.currentPage, this.pageSize, page => {
                    this.currentPage = page;
                    this.loadData();
                });

                // 初始化 Popover
                initPopover();
            })
            .catch(error => console.error("Error loading bills:", error));
    },

    _toggleTable() {
        document.getElementById('home-card').classList.add('d-none');
        document.getElementById('operate-area').classList.remove('d-none');
        document.getElementById('user-table').classList.add('d-none');
        document.getElementById('goods-table').classList.add('d-none');
        document.getElementById('salebill-table').classList.remove('d-none');
        document.getElementById('pagination-nav').classList.remove('d-none');
    },

    bindEvents() {
        document.querySelectorAll(`#${this.tableId} .edit-btn`).forEach(button => {
            button.addEventListener("click", this._editHandler.bind(this));
        });

        document.querySelectorAll(`#${this.tableId} .delete-btn`).forEach(button => {
            button.addEventListener("click", this._deleteHandler.bind(this));
        });

        document.getElementById("add-record-button").addEventListener("click", this._addHandler.bind(this));

        // 绑定搜索与重置事件监听
        const searchButton = document.getElementById("search-button");
        searchButton.addEventListener("click", (event) => _searchButtonClickHandler(event, SaleBillModule));

        const resetButton = document.getElementById("reset-button");
        resetButton.addEventListener("click", (event) => _resetButtonClickHandler(event, SaleBillModule));

    },

    _editHandler(event) {
        const id = event.target.getAttribute('data-id');
        this.openOffcanvas("修改账单", id);
    },

    _deleteHandler(event) {
        const button = event.currentTarget;
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
                const id = button.getAttribute('data-id');
                this.handleDelete(id);
                popoverInstance.hide();
            });
        });

        popoverInstance.show();
    },

    _addHandler() {
        this.openOffcanvas("添加账单");
    },

    openOffcanvas(title, id = null) {
        const offcanvasElement = document.getElementById("offcanvasForm");
        offcanvasElement.querySelector('.offcanvas-title').innerHTML = title;

        offcanvasElement.querySelector("#offcanvas-form").innerHTML = `
            <div class="mb-3">
                <label for="goods_no" class="form-label">商品编号</label>
                <input type="text" class="form-control" id="goods_no" name="goods_no" ${id ? 'readonly' : ''}
                            placeholder="请输入已有的商品编号">
            </div>
            <div class="mb-3">
                <label for="card_no" class="form-label">卡号</label>
                <input type="text" class="form-control" id="card_no" name="card_no" ${id ? 'readonly' : ''}
                            placeholder="请输入已有的用户卡号">
            </div>
            <div class="mb-3">
                <label for="number" class="form-label">数量</label>
                <input type="number" class="form-control" id="number" name="number">
            </div>
            <div class="mb-3">
                <label for="total_price" class="form-label">总价</label>
                <input type="number" class="form-control" id="total_price" name="total_price">
            </div>
            <div class="mb-3">
                <label for="bill_date" class="form-label">账单日期</label>
                <input type="date" class="form-control" id="bill_date" name="bill_date">
            </div>
        `;

        document.querySelectorAll('.offcanvas-backdrop').forEach(backdrop => backdrop.remove());
        const offcanvas = new bootstrap.Offcanvas(offcanvasElement);
        offcanvas.show();

        if (id) this.loadBillData(id);

        // 清除 submitButton 的所有事件监听器
        const submitButton = document.getElementById("submitFormButton");
        const newSubmitButton = submitButton.cloneNode(true); // 克隆一个新的按钮
        submitButton.parentNode.replaceChild(newSubmitButton, submitButton);

        // 为新按钮添加事件监听器
        newSubmitButton.addEventListener("click", () => {
            this.saveBillData(id);
            offcanvas.hide();
        });

        offcanvasElement.addEventListener('hidden.bs.offcanvas', () => {
            document.querySelectorAll('.offcanvas-backdrop').forEach(backdrop => backdrop.remove());
        });
    },

    loadBillData(id) {
        const bill = this.cachedData.find(item => item.goods_no === id);
        if (bill) {
            document.getElementById("goods_no").value = bill.goods_no;
            document.getElementById("card_no").value = bill.card_no;
            document.getElementById("number").value = bill.number;
            document.getElementById("total_price").value = bill.total_price;
            document.getElementById("bill_date").value = bill.bill_date.split(" ")[0];
        } else {
            showToast("加载失败", "未找到账单信息，请重试。", false);
        }
    },

    saveBillData(id) {
        const formData = new FormData(document.getElementById("offcanvas-form"));
        const data = Object.fromEntries(formData.entries());
        const url = id ? `${this.updateUrl}?id=${id}` : this.addUrl;

        fetch(url, {
            method: "post",
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(data => {
                showToast(data.success ? "成功" : "失败", data.message, data.success);
                if (data.success) this.loadData();
            })
            .catch(error => console.error("Error saving bill data:", error));
    },

    handleDelete(id) {
        fetch(`${this.deleteUrl}?id=${id}`, { method: "DELETE" })
            .then(response => response.json())
            .then(data => {
                showToast(data.success ? "删除成功" : "删除失败", data.message, data.success);
                if (data.success) this.loadData();
            })
            .catch(error => console.error("Error deleting bill:", error));
    }
};

document.getElementById("bill-info").addEventListener("click", () => SaleBillModule.loadData());
