<template>
    <div id="platform">
        <div id="openheader">
            <div id="title">
                쉽고 빠른 블로그 개설
            </div>
            web을 몰라도 누구든지 쉽게 제작할 수 있습니다
        </div>
        <div class="row">
            <div class="col-7 border">
                <!-- 블로그명 섹션-->
                <div class="row">
                    <div class="col-10 pr-0">
                        <div id="section">
                            <input
                                id="input"
                                type="text"
                                placeholder="블로그명"
                            >
                        </div>
                        <div class="col-2">
                            빈칸
                        </div>
                    </div>
                    <!-- 유저 추가 섹션-->
                    <div
                        id="section"
                        class="d-flex justify-content-between"
                    >
                        <input
                            id="input"
                            type="text"
                        >
                        <b-button
                            id="plusbutton"
                        >
                            유저 추가 <i class="fas fa-plus" />
                        </b-button>
                    </div>
                </div>
            </div>
            <div class="col-5 d-flex">
                <div id="photo">
                    블로그 프로필 사진
                </div>
                <input type="file">
            </div>
        </div>
    </div>
</template>

<script>
export default {

};
</script>

<style scoped>
#openheader {
  margin-bottom: 50px;
}

#section {
  width: 100%;
  height: 30px;
  margin: 10px 0;
  border: 1px solid #b6b6b6;
  border-radius: 5px;
}
#input {
  width: 100%;
  border: 0;
}

#plusbutton {
  float: right;
  background-color:rgb(178, 87, 34);
  height: 30px;
  font-size: 2vh;
  margin: 10px;
}

#photo {
  height: 200px;
  width: 200px;
  border: 1px solid #b6b6b6;
}

</style>
