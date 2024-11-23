// 商品模块
const GoodsModule = {
    selectUrl: "select/goods", // 查询商品数据的URL
    deleteUrl: "delete/goods", // 删除商品数据的URL
    addUrl: "add/goods", // 添加商品的URL
    updateUrl: "update/goods", // 更新商品的URL
    findUrl: "find/goods", // 查找商品的URL
    uploadUrl: "upload", // 图片上传URL
    imageDeleteUrl: "deleteTempFile", // 临时图片删除的URL
    tableId: "goods-table", // 商品表格ID
    currentPage: 1, // 当前页码
    pageSize: 5, // 每页显示商品数量
    cachedData: null, // 缓存的商品数据
    uploadedImageUrl: "", // 缓存已上传图片的URL
    keyword: null, // 关键词

    // 加载商品数据
    loadData() {
        this._toggleTable(); // 切换到商品表格视图
        fetch(`${this.selectUrl}?page=${this.currentPage}&pageSize=${this.pageSize}&keyword=${this.keyword}`)
            .then(response => response.json())
            .then(result => {
                const data = result.data;
                const totalItems = result.totalItems;
                this.cachedData = data;
                const tbody = document.querySelector(`#${this.tableId} tbody`);
                tbody.innerHTML = "";

                // 渲染商品数据到表格
                data.forEach(item => {
                    const row = `<tr>
                        <td>${item.goods_no}</td>
                        <td>${item.goods_name}</td>
                        <td>${item.in_price}</td>
                        <td>${item.sale_price}</td>
                        <td>${item.number}</td>
                        <td><img src="${item.img_url}" alt="${item.goods_name}" style="width: 50px; height: 50px; object-fit: cover;"></td>
                        <td>
                            <button class="btn btn-warning btn-sm edit-btn" data-id="${item.goods_no}">修改</button>
                            <button class="btn btn-danger btn-sm delete-btn"
                                data-id="${item.goods_no}" data-bs-toggle="popover"
                                title="确认删除？"
                                data-bs-content="
                                <button class='btn btn-sm btn-secondary cancel-btn'>取消</button>
                                <button class='btn btn-sm btn-danger confirm-delete-btn'>确认</button>"
                            >删除</button>
                        </td>
                    </tr>`;
                    tbody.innerHTML += row;
                });
                _removeEventListeners();
                this.bindEvents(); // 绑定事件

                // 调用全局分页器
                updatePagination(totalItems, this.currentPage, this.pageSize, (page) => {
                    this.currentPage = page;
                    this.loadData();
                });

                // 初始化 Popover
                initPopover();
            })
            .catch(error => console.error("Error loading goods data:", error));
    },

    // 切换到商品表格视图
    _toggleTable() {
        document.getElementById('home-card').classList.add('d-none');
        document.getElementById('operate-area').classList.remove('d-none');
        document.getElementById('user-table').classList.add('d-none');
        document.getElementById('goods-table').classList.remove('d-none');
        document.getElementById('salebill-table').classList.add('d-none');
        document.getElementById('pagination-nav').classList.remove('d-none');
    },
    // 移除之前模块的事件监听器
    _removeEventListeners() {
        // 移除之前绑定的事件监听器
        document.querySelectorAll(`#${this.tableId} .edit-btn`).forEach(button => {
            button.removeEventListener("click", this._editEventHandler);
        });

        document.querySelectorAll(`#${this.tableId} .delete-btn`).forEach(button => {
            button.removeEventListener("click", this._deleteEventHandler);
        });

        document.getElementById("add-record-button")?.removeEventListener("click", this._addEventHandler);
    },
    // 绑定表格内的编辑和删除按钮事件
    bindEvents() {
        // 修改按钮点击事件
        document.querySelectorAll(`#${this.tableId} .edit-btn`).forEach(button => {
            button.addEventListener("click", event => {
                const id = event.target.getAttribute('data-id');
                this.openOffcanvas("修改商品", id);
            });
        });

        // 监听删除按钮点击事件
        document.querySelectorAll(`#${this.tableId} .delete-btn`).forEach(button => {
            button.addEventListener("click", event => {
                const popoverInstance = new bootstrap.Popover(button, {
                    html: true,
                    sanitize: false
                });

                button.addEventListener('shown.bs.popover', () => {
                    const popoverContent = document.querySelector('.popover-body');
                    // Popover组件确认/取消按钮监听器
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

        // 添加商品按钮事件
        document.getElementById("add-record-button").addEventListener("click", () => {
            this.openOffcanvas("添加商品");
        });

        // 绑定搜索与重置按钮事件监听
        const searchButton = document.getElementById("search-button");
        searchButton.addEventListener("click", (event) => _searchButtonClickHandler(event, GoodsModule));

        const resetButton = document.getElementById("reset-button");
        resetButton.addEventListener("click", (event) => _resetButtonClickHandler(event, GoodsModule));

    },

    // 打开侧边栏表单（用于添加和修改商品）
    openOffcanvas(title, id = null) {
        const offcanvasElement = document.getElementById("offcanvasForm");
        offcanvasElement.querySelector('.offcanvas-title').innerHTML = title;
        this.uploadedImageUrl = ""; // 重置上传的图片URL

        const isAdding = (title === '添加商品');
        // 填充表单内容
        offcanvasElement.querySelector("#offcanvas-form").innerHTML = `
            <div class="mb-3">
                <label for="goods_no" class="form-label">商品编号</label>
                <input type="text" class="form-control" id="goods_no" name="goods_no" required>
            </div>
            <div class="mb-3">
                <label for="goods_name" class="form-label">商品名称</label>
                <input type="text" class="form-control" id="goods_name" name="goods_name">
            </div>
            <div class="mb-3">
                <label for="in_price" class="form-label">进价</label>
                <input type="number" class="form-control" id="in_price" name="in_price">
            </div>
            <div class="mb-3">
                <label for="sale_price" class="form-label">售价</label>
                <input type="number" class="form-control" id="sale_price" name="sale_price">
            </div>
            <div class="mb-3">
                <label for="number" class="form-label">数量</label>
                <input type="number" class="form-control" id="number" name="number">
            </div>
            <div class="mb-3">
                <label for="image_upload" class="form-label">商品图片</label>
                <div id="image-upload-container">
                    <div class="layui-upload-drag" id="upload-image">
                        <p>点击上传，或将图片拖拽到此处</p>
                    </div>
                    <input type="hidden" id="img_url" name="img_url">
                    <button id="remove-image" class="btn btn-secondary btn-sm mt-2 d-none">删除图片</button>
                </div>
            </div>
        `;

        document.querySelectorAll('.offcanvas-backdrop').forEach(backdrop => backdrop.remove());
        const offcanvas = new bootstrap.Offcanvas(offcanvasElement);
        offcanvas.show();

        if (id) {
            this.loadGoodsData(id); // 如果是编辑模式，加载商品数据
            // 设置 goods_no 为只读
            document.getElementById("goods_no").readOnly = true;
        } else {
            // 新增模式下取消只读限制
            document.getElementById("goods_no").readOnly = false;
        }

        // 设置提交表单事件
        // 清除 submitButton 的所有事件监听器
        const submitButton = document.getElementById("submitFormButton");
        const newSubmitButton = submitButton.cloneNode(true); // 克隆一个新的按钮
        submitButton.parentNode.replaceChild(newSubmitButton, submitButton);

        // 为新按钮添加事件监听器
        newSubmitButton.addEventListener("click", () => {
            this.saveGoodsData(id);
            offcanvas.hide();
        });

        offcanvasElement.addEventListener('hidden.bs.offcanvas', () => {
            // 在取消或关闭侧边栏时移除上传的图片
            this.removeTemporaryImage();
            document.querySelectorAll('.offcanvas-backdrop').forEach(backdrop => backdrop.remove());
        });

        // 使用layui上传组件初始化图片上传
        layui.upload.render({
            elem: '#upload-image',
            url: this.uploadUrl,
            done: (res) => {
                if (res.success) {
                    document.getElementById("img_url").value = res.url;
                    document.getElementById("upload-image").innerHTML = `<img src="${res.url}" style="width: 100%; height: 100%; object-fit: cover;">`;
                    document.getElementById("remove-image").classList.remove("d-none");
                } else {
                    alert("图片上传失败，请重试！");
                }
            }
        });

        // 删除已上传图片
        document.getElementById("remove-image").addEventListener("click", () => {
            document.getElementById("img_url").value = "";
            document.getElementById("upload-image").innerHTML = `<p>点击上传，或将图片拖拽到此处</p>`;
            document.getElementById("remove-image").classList.add("d-none");

            this.removeTemporaryImage();
        });


    },

    // 删除临时上传的图片
    removeTemporaryImage() {
        if (this.uploadedImageUrl) {
            fetch(`${this.imageDeleteUrl}?url=${encodeURIComponent(this.uploadedImageUrl)}`, { method: "DELETE" })
                .then(response => response.json())
                .then(result => {
                    if (result.success) {
                        console.log("临时图片已删除");
                    } else {
                        console.warn("临时图片删除失败", result.message);
                    }
                })
                .catch(error => console.error("Error deleting image:", error));
            this.uploadedImageUrl = ""; // 重置图片URL
        }
    },

    // 加载商品数据（用于编辑时填充表单）
    loadGoodsData(id) {
        const goods = this.cachedData.find(item => item.goods_no === id);
        if (goods) {
            document.getElementById('goods_no').value = goods.goods_no;
            document.getElementById("goods_name").value = goods.goods_name;
            document.getElementById("in_price").value = goods.in_price;
            document.getElementById("sale_price").value = goods.sale_price;
            document.getElementById("number").value = goods.number;

            if (goods.img_url) {
                document.getElementById("img_url").value = goods.img_url;
                document.getElementById("upload-image").innerHTML = `<img src="${goods.img_url}" style="width: 100%; height: 100%; object-fit: cover;">`;
                document.getElementById("remove-image").classList.remove("d-none");
            }
        } else {
            showToast("加载失败", "未找到商品信息，请重试。", false);
        }
    },

    // 保存商品数据
    saveGoodsData(id) {
        const data = {
            goods_no: document.getElementById("goods_no").value,
            goods_name: document.getElementById("goods_name").value,
            in_price: parseFloat(document.getElementById("in_price").value),
            sale_price: parseFloat(document.getElementById("sale_price").value),
            number: parseInt(document.getElementById("number").value, 10),
            img_url: document.getElementById("img_url").value,
        };

        const url = id ? this.updateUrl : this.addUrl;

        fetch(url, {
            method: "post",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data),
        })
            .then(response => response.json())
            .then(result => {
                if (result.success) {
                    this.loadData(); // 重新加载表格
                    showToast("成功", id ? "商品已更新！" : "商品已添加！", true);
                } else {
                    showToast("失败", result.message || "操作失败，请重试。", false);
                }
            })
            .catch(error => console.error("Error saving goods data:", error));
    },

    // 删除商品
    handleDelete(id, button) {
        fetch(`${this.deleteUrl}?id=${id}`, { method: "delete" })
            .then(response => response.json())
            .then(result => {
                if (result.success) {
                    this.loadData(); // 重新加载表格
                    showToast("成功", "商品已删除！", true);
                } else {
                    showToast("失败", result.message || "删除失败，请重试。", false);
                }
            })
            .catch(error => console.error("Error deleting goods data:", error));
        this.removeTemporaryImage();
    },
};

document.getElementById("product-info").addEventListener("click", () => GoodsModule.loadData());
