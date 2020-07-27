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
                    <b-card
                        img-src="https://placekitten.com/300/300"
                        img-alt="Card image"
                        img-left
                        class="mb-3"
                    >
                        <b-card-text>
                            Some quick example text to build on the card and
                            make up the bulk of the card's content.
                        </b-card-text>
                    </b-card>
                </div>
            </div>
            <div class="border m-3 w-25">
                광고 배너
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'List',
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
