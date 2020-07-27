<template>
    <div>
        <div id="openheader">
            <div id="title">
                쉽고 빠른 블로그 개설
            </div>
            web을 몰라도 누구든지 쉽게 제작할 수 있습니다
        </div>
        <div
            id="openheader"
            class="row"
        >
            <div class="col-8">
                <div class="row">
                    <div
                        id="section"
                        class="col-8"
                    >
                        <input
                            type="text"
                            placeholder="블로그명"
                        >
                        <input
                            v-model="user"
                            type="text"
                            placeholder="회원 이름"
                        >
                    </div>
                    <div class="col-4 d-flex align-items-end">
                        <button
                            id="plusbutton"
                            @click="adduser"
                        >
                            회원 추가 <i class="fas fa-plus" />
                        </button>
                    </div>
                </div>
            </div>
            <div class="col-4 d-flex">
                <div class="row">
                    <div class="col-8">
                        <div id="photo">
                            블로그 프로필 사진
                        </div>
                    </div>
                </div>
                <div class="col-4">
                    <input type="file">
                </div>
            </div>
        </div>
        <div
            id="openheader"
            class="d-flex"
        >
            블로그테마
        </div>
        <div class="row">
            <div class="col-3">
                <b-form-checkbox> 테마1 </b-form-checkbox>
                <div
                    id="photo"
                    class="border"
                />
            </div>
            <div class="col-3">
                <b-form-checkbox> 테마2 </b-form-checkbox>
                <div
                    id="photo"
                    class="border"
                />
            </div>
            <div class="col-3">
                <b-form-checkbox>사용자 설정</b-form-checkbox>
                <div
                    id="photo"
                    class="border d-flex justify-content-center align-items-center"
                >
                    <i class="fas fa-question" />
                </div>
            </div>
            <div
                class="col-2 d-flex align-items-center px-0"
            >
                <button id="openbutton">
                    개설하기
                </button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'Open',
    data() {
        return {
            user: '',
        };
    },
    methods: {
        adduser() {

        },
    },
};
</script>

<style scoped>
#openheader {
  margin-bottom: 50px;
}

#section {
  padding-right: 0;
}

#plusbutton {
  border: 1px solid #b6b6b6;
  color: white;
  border-radius: 10px;
  background-color:rgb(178, 87, 34);
  font-size: 2vh;
}

#photo {
  height: 200px;
  width: 200px;
  border: 1px solid #b6b6b6;
}

#openbutton {
    border: 1px solid #b6b6b6;
    color: white;
    font-size:3vh;
    height: 8vh;
    background-color:rgb(178, 87, 34);
    border-radius: 10px;
    text-decoration: none;
  }

</style>
