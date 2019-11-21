/**
 * 文件选择扩展模块
 * date:2019-06-27   License By http://easyweb.vip
 */
layui.define(['jquery', 'layer', 'form', 'upload', 'laytpl', 'util'], function (exports) {
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form;
    var upload = layui.upload;
    var laytpl = layui.laytpl;
    var util = layui.util;

    var fileChoose = {};

    // 打开选择文件弹窗
    fileChoose.open = function (param) {
        var accept = param.accept;  // 文件类型
        var exts = param.exts;  // 文件后缀
        var multi = param.multi;  // 是否多选
        var maxNum = param.maxNum;  // 最大选择数量
        var fileServer = param.fileServer;  // 文件服务器url
        var onChoose = param.onChoose;  // 选择回调

        accept || (accept = 'file');

        layer.open({
            type: 1,
            title: '选择文件',
            content: getHtml(),
            area: ['600px', '420px'],
            offset: '50px',
            shade: .1,
            fixed: false,
            success: function () {
                init();
            }
        });

        // 渲染文件列表
        function renderList(dir) {
            dir || (dir = $('#fc-current-position').text());
            layer.load(2);
            $.get(fileServer + 'api/list', {
                dir: dir,
                accept: accept,
                exts: exts
            }, function (res) {
                layer.closeAll('loading');
                if (res.code == 200) {
                    var html = '';
                    if (res.data.length <= 0) {
                        html += '<div class="file-choose-empty">';
                        html += '   <i class="layui-icon layui-icon-face-surprised"></i>';
                        html += '   <p>没有文件</p>';
                        html += '</div>';
                    } else {
                        for (var i = 0; i < res.data.length; i++) {
                            res.data[i].url = fileServer + 'file/' + res.data[i].url;
                            res.data[i].smUrl = fileServer + 'file/' + res.data[i].smUrl;
                            var item = res.data[i];
                            html += '<div class="file-choose-list-item" data-index="' + i + '">';
                            var url = item.hasSm ? item.smUrl : ('assets/images/fti/' + item.type + '.png');
                            html += '   <div class="file-choose-list-item-img' + item.hasSm ? '' : ' img-icon' + '" style="background-image: url(\'' + url + '\')"></div>';
                            html += '   <div class="file-choose-list-item-name">' + item.name + '</div>';
                            if (!item.isDir && multi == 'true') {
                                html += '   <div class="file-choose-list-item-ck layui-form">';
                                html += '       <input type="checkbox" lay-skin="primary" lay-filter="file-choose-item-ck"/>';
                                html += '   </div>';
                            }
                            html += '</div>';
                        }
                    }
                    $('#fc-btn-ok-sel').text('完成选择');
                    $('#file-choose-list').html(html);
                    form.render('checkbox');
                } else {
                    layer.msg(res.msg, {icon: 2});
                }
            });
        }

        // 事件处理
        function init() {
            (multi && multi == 'true') && ($('body').addClass('showBB'));
            renderList();
            // 上传文件事件
            var nExts;
            if (exts) {
                nExts = exts.replace(/,/g, "|");
            }
            upload.render({
                elem: '#fc-btn-upload',
                url: fileServer + 'file/upload',
                choose: function (obj) {
                    layer.load(2);
                },
                done: function (res, index, upload) {
                    layer.closeAll('loading');
                    if (res.code != 200) {
                        layer.msg(res.msg, {icon: 2});
                    } else {
                        layer.msg(res.msg, {icon: 1});
                        $('#fc-current-position').text(util.toDateString(new Date(), '/yyyy/MM/dd'));
                        renderList();
                    }
                },
                error: function () {
                    layer.closeAll('loading');
                    layer.msg('上传失败', {icon: 2});
                },
                accept: accept == 'image' ? 'imagess' : accept,
                exts: nExts
            });

            // 刷新
            $('#fc-btn-refresh').click(function () {
                renderList();
            });

            var mUrl;
            // 列表点击事件
            $('body').on('click', '.fc-file-list-group-item', function (e) {
                var isDir = $(this).data('dir');
                var name = $(this).data('name');
                mUrl = $(this).data('url');
                $('#copy').attr('data-clipboard-text', mUrl);
                if (isDir) {
                    var cDir = $('#fc-current-position').text();
                    cDir += (cDir == '/' ? name : ('/' + name));
                    $('#fc-current-position').text(cDir);
                    renderList(cDir);
                } else {
                    var $target = $(this).find('.fc-file-list-group-img');
                    $('#fc-dropdown-choose').css({
                        'top': $target.offset().top + 90,
                        'left': $target.offset().left + 25
                    });
                    $('#fc-dropdown-choose').addClass('dropdown-opened');
                    if (e !== void 0) {
                        e.preventDefault();
                        e.stopPropagation();
                    }
                }
            });

            // 返回上级
            $('#fc-btn-back').click(function () {
                var cDir = $('#fc-current-position').text();
                if (cDir == '/') {
                    // layer.msg('已经是根目录', {icon: 2})
                } else {
                    cDir = cDir.substring(0, cDir.lastIndexOf('/'));
                    if (!cDir) {
                        cDir = '/';
                    }
                    $('#fc-current-position').text(cDir);
                    renderList(cDir);
                }
            });

            // 点击空白隐藏下拉框
            $('html').off('click.dropdown').on('click.dropdown', function () {
                $('#copy').attr('data-clipboard-text', '');
                $('#fc-dropdown-choose').removeClass('dropdown-opened');
            });

            // 打开
            $('#fc-dropdown-btn-open').click(function () {
                window.open(mUrl);
            });
            // 选择
            $('#fc-dropdown-btn-sel').click(function () {
                if (!multi || multi == 'false') {
                    var urls = [];
                    urls.push(mUrl);
                    okChoose(urls);
                } else {
                    $('.fc-file-list-group-item[data-url="' + mUrl + '"] .layui-form-checkbox').trigger('click');
                }
            });

            // 多选框事件
            $('body').on('click', '.fc-file-list-group-ck', function (e) {
                if (e !== void 0) {
                    e.preventDefault();
                    e.stopPropagation();
                }
            });

            // 完成选择按钮
            $('#fc-btn-ok-sel').click(function () {
                var urls = [];
                $('input[name="imgCk"]:checked').each(function () {
                    urls.push($(this).parents('.fc-file-list-group-item').data('url'));
                });
                if (urls.length <= 0) {
                    layer.msg('请选择', {icon: 2});
                    return;
                }
                if (maxNum && parseInt(maxNum) > 1 && urls.length > maxNum) {
                    layer.msg('最多只能选择' + maxNum + '个', {icon: 2});
                    return;
                }
                okChoose(urls);
            });

            // 监听复选框选中
            form.on('checkbox(imgCk)', function (data) {
                var ckSize = $('input[name="imgCk"]:checked').length;
                if (data.elem.checked) {
                    if (maxNum && parseInt(maxNum) > 1 && ckSize > maxNum) {
                        layer.msg('最多只能选择' + maxNum + '个', {icon: 2});
                        $(data.elem).prop('checked', false);
                        form.render('checkbox');
                        return;
                    }
                    $(data.elem).parents('.fc-file-list-group-item').addClass('active');
                } else {
                    $(data.elem).parents('.fc-file-list-group-item').removeClass('active');
                }
                $('#fc-btn-ok-sel').text('完成选择(' + ckSize + ')');
            });
        }

        // 完成选择
        function okChoose(urls) {
            onChoose && onChoose(urls);
            var id = $(elem).parents('.layui-layer').attr('id').substring(11);
            layer.close(id);
        }

    };

    // 获取页面html
    function getHtml() {
        var html = '';
        // 样式
        html += '<style>';
        html += '.file-choose {';
        html += '   position: relative;';
        html += '   background: #fff;';
        html += '   height: 100%;';
        html += '}';

        html += '.file-choose-top-bar {';
        html += '   position: relative;';
        html += '}';

        html += '.file-choose-top-text {';
        html += '   padding: 12px;';
        html += '}';

        html += '.file-choose-top-btn-group {';
        html += '   position: absolute;';
        html += '   right: 12px;';
        html += '   top: 5px;';
        html += '}';

        html += '.file-choose-list {';
        html += '   position: absolute;';
        html += '   top: 40px;';
        html += '   bottom: 48px;';
        html += '   left: 0;';
        html += '   right: 0;';
        html += '   overflow: auto;';
        html += '   padding: 0 8px;';
        html += '}';

        html += '.file-choose-bottom-bar {';
        html += '   position: absolute;';
        html += '   left: 0;';
        html += '   right: 0;';
        html += '   bottom: 0;';
        html += '   border-top: 1px solid #eee;';
        html += '   padding: 8px 12px;';
        html += '   text-align: right;';
        html += '}';

        html += '.file-choose-list-item {';
        html += '   position: relative;';
        html += '   display: inline-block;';
        html += '   vertical-align: top;';
        html += '   padding: 15px 8px;';
        html += '}';

        html += '.file-choose-list-item-img {';
        html += '   width: 90px;';
        html += '   height: 90px;';
        html += '   background-repeat: no-repeat;';
        html += '   background-position: center;';
        html += '   background-size: cover;';
        html += '   border-radius: 3px;';
        html += '   overflow: hidden;';
        html += '   position: relative;';
        html += '}';

        html += '.file-choose-list-item-img.img-icon {';
        html += '   background-size: inherit;';
        html += '}';

        html += '.file-choose-list-item-img.active:after {';
        html += '   content: "";';
        html += '   position: absolute;';
        html += '   left: 0;';
        html += '   top: 0;';
        html += '   bottom: 0;';
        html += '   right: 0;';
        html += '   background: rgba(0, 0, 0, 0.3);';
        html += '}';

        html += '.file-choose-list-item-name {';
        html += '   width: 90px;';
        html += '   overflow: hidden;';
        html += '   text-overflow: ellipsis;';
        html += '   white-space: nowrap;';
        html += '   color: #333;';
        html += '   font-size: 12px;';
        html += '   text-align: center;';
        html += '   margin-top: 12px;';
        html += '}';

        html += '.file-choose-list-item-ck {';
        html += '   position: absolute;';
        html += '   right: 8px;';
        html += '   top: 15px;';
        html += '}';

        html += '.file-choose-list-item-ck .layui-form-checkbox {';
        html += '   padding: 0;';
        html += '}';
        html += '</style>';
        // html
        html += '<div class="file-choose">';
        html += '    <div class="file-choose-top-bar">';
        html += '        <div class="file-choose-top-text">当前位置：<span id="fc-current-position">/</span></div>';
        html += '        <div class="file-choose-top-btn-group">';
        html += '            <button id="fc-btn-back" class="layui-btn layui-btn-sm layui-btn-primary icon-btn">';
        html += '                <i class="layui-icon">&#xe65c;</i>上级';
        html += '            </button>';
        html += '            <button id="fc-btn-refresh" class="layui-btn layui-btn-sm layui-btn-primary icon-btn">';
        html += '                <i class="layui-icon">&#xe669;</i>刷新';
        html += '            </button>';
        html += '            <button id="fc-btn-upload" class="layui-btn layui-btn-sm icon-btn" style="margin-right: 0;">';
        html += '                <i class="layui-icon">&#xe681;</i>上传';
        html += '            </button>';
        html += '        </div>';
        html += '    </div>';
        html += '    <div id="file-choose-list" class="file-choose-list"></div>';
        html += '    <div class="file-choose-bottom-bar">';
        html += '        <button id="fc-btn-ok-sel" class="layui-btn layui-btn-sm layui-btn-normal icon-btn">完成选择</button>';
        html += '    </div>';
        html += '</div>';

        return html;
    }

    exports("fileChoose", fileChoose);
});

