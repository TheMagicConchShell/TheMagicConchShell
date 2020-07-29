<template>
    <div>
        고민게시판
        <div class="d-flex">
            <div id="category">
                고민 카테고리
                <ul>
                    <li>연애</li>
                    <li>일상</li>
                    <li>직장</li>
                    <li>결혼</li>
                    <li>문화</li>
                    <li>주식</li>
                    <li>치정</li>
                </ul>
                <div class="border h-25">
                    광고 배너
                </div>
            </div>
            <div id="article">
                <ul id="tab">
                    <li style="background-color:#6B799E">
                        실시간 인기
                    </li>
                    <li>연애</li>
                    <li>일상</li>
                    <li>직장</li>
                    <li>결혼</li>
                    <li>문화</li>
                    <li>주식</li>
                    <li>치정</li>
                </ul>
                <div>
                    <counsel-board
                        :page="page"
                    />
                </div>
            </div>
            <div class="border m-3 w-25">
                광고 배너
            </div>
        </div>
    </div>
</template>

<script>
import CounselBoard from '@/views/counsel/CounselBoard.vue';

export default {
    name: 'List',
    components: {
        CounselBoard,
    },
    data: () => ({
        page: 0,
        pageCount: 1,
    }),
    methods: {
        write() {
            this.$router.push({
                name: 'PostWrite'
            });
        },
    },
};
</script>

<style scoped>
#category {
    float: left;
    width: 10%;
    max-height: 100vh;
    border: 1px solid;
}
#tab {
    display:flex;
    padding: 0;
    width: 100%;
}
#tab li{
    clear: both;
    min-width: 12.5%;
    padding: 0 20px;
    display: inline-block;
    border: 1px solid;
}
#article {
    margin-left: 20px;
}
</style>
