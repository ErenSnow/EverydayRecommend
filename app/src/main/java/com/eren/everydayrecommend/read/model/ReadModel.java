package com.eren.everydayrecommend.read.model;

import java.util.List;

/**
 * 作者：Leon
 * 描述:
 */
public class ReadModel {

    /**
     * code : 200
     * msg : success
     * newslist : [{"ctime":"2017-06-06 00:06","title":"菜鸟顺丰之争落幕：双方仍需合作 谁会和钱过不","description":"移动互联","picUrl":"http://cms-bucket.nosdn.127.net/958b90795cdd4c94875785bf4d022f4420170605233544.png","url":"http://tech.163.com/17/0606/00/CM74FLC800097U7R.html"},{"ctime":"2017-06-06 00:15","title":"三大比特币平台反洗钱系统升级 恢复提现后涨25%","description":"移动互联","picUrl":"http://cms-bucket.nosdn.127.net/catchpic/0/0e/0e72e6aeb43e99da2964123020a8fcb0.jpg","url":"http://tech.163.com/17/0606/00/CM750JO300097U7R.html"},{"ctime":"2017-06-05 23:35","title":"亚马逊之后，谷歌股价终于也突破1000美元大关","description":"移动互联","picUrl":"http://cms-bucket.nosdn.127.net/958b90795cdd4c94875785bf4d022f4420170605233544.png","url":"http://tech.163.com/17/0605/23/CM72NV9C00097U7R.html"},{"ctime":"2017-06-05 20:22","title":"国家互金专委会推出理财安全助手APP","description":"移动互联","picUrl":"http://cms-bucket.nosdn.127.net/b0cfb3fa881f4f3bb6633ecc648b14cf20170605202033.png","url":"http://tech.163.com/17/0605/20/CM6NMAVA00097U7R.html"},{"ctime":"2017-06-05 20:25","title":"五年之后，网易新闻为什么彻底放弃了\u201c有态度\u201d","description":"移动互联","picUrl":"http://cms-bucket.nosdn.127.net/catchpic/d/d7/d79afce5417a269dba6cbdf21d2666a4.jpg","url":"http://tech.163.com/17/0605/20/CM6NRMAT00097U7R.html"},{"ctime":"2017-06-05 16:23","title":"美团外卖称日交易额已破5亿 平均客单价超40元　","description":"移动互联","picUrl":"http://cms-bucket.nosdn.127.net/5bf5bf24c9bc4ba3adada863248aedf820170605153933.png","url":"http://tech.163.com/17/0605/16/CM6A1DSC00097U7R.html"},{"ctime":"2017-06-05 16:26","title":"科技晚报:苹果WWDC将开幕|郭台铭:电商平台将消","description":"移动互联","picUrl":"http://cms-bucket.nosdn.127.net/eee6187df8d840d79568f828a761ecf120170605162258.png","url":"http://tech.163.com/17/0605/16/CM6A59I000097U7R.html"},{"ctime":"2017-06-05 16:44","title":"变更经营范围 赵本山要转战互联网了？","description":"移动互联","picUrl":"http://cms-bucket.nosdn.127.net/ed2e7112283541668d5e139bbdb2f68420170605164408.png","url":"http://tech.163.com/17/0605/16/CM6B6VK900097U7R.html"},{"ctime":"2017-06-05 15:26","title":"全国学生资助管理中心：毕业生要谨防电信诈骗","description":"通信行业","picUrl":"http://cms-bucket.nosdn.127.net/373de6e44e7f43809360abea67e788fb20170605141426.png","url":"http://tech.163.com/17/0605/15/CM66OK0K00097U7S.html"},{"ctime":"2017-06-05 15:43","title":"今年我国将发射6至8颗北斗三号卫星 将服务全球","description":"通信行业","picUrl":"http://cms-bucket.nosdn.127.net/175fb475a3464fc08c745f19e2f21f9e20170605154345.png","url":"http://tech.163.com/17/0605/15/CM67NUB100097U7S.html"}]
     */

    private int code;
    private String msg;
    private List<NewslistEntity> newslist;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setNewslist(List<NewslistEntity> newslist) {
        this.newslist = newslist;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<NewslistEntity> getNewslist() {
        return newslist;
    }

    public static class NewslistEntity {
        /**
         * ctime : 2017-06-06 00:06
         * title : 菜鸟顺丰之争落幕：双方仍需合作 谁会和钱过不
         * description : 移动互联
         * picUrl : http://cms-bucket.nosdn.127.net/958b90795cdd4c94875785bf4d022f4420170605233544.png
         * url : http://tech.163.com/17/0606/00/CM74FLC800097U7R.html
         */

        private String ctime;
        private String title;
        private String description;
        private String picUrl;
        private String url;

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCtime() {
            return ctime;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public String getUrl() {
            return url;
        }
    }
}
