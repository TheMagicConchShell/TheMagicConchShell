<template>
    <div>
        <template v-if="list && list.length">
            <table class="table">
                <thead class="thead-light">
                    <tr>
                        <th>
                            순위
                        </th>
                        <th>
                            제목
                        </th>
                        <th>
                            작성자
                        </th>
                        <th>
                            추천수
                            <i 
                                v-if="mySort==6" 
                                class="fas fa-long-arrow-alt-down"
                            />
                        </th>
                        <th>
                            조회수
                            <i 
                                v-if="mySort==7" 
                                class="fas fa-long-arrow-alt-down"
                            />
                        </th>
                        <th>
                            답변수
                            <i 
                                v-if="mySort==8" 
                                class="fas fa-long-arrow-alt-down"
                            />
                        </th>
                        <th>
                            작성날짜
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr 
                        v-for="(item,index) in list"
                        :key="item.nickname"
                    >
                        <td>{{ index+1 }}</td>
                        <td>
                            <router-link
                                :to="{name: 'CounselDetail', params: {no: item.no}}"
                            >
                                [{{ item.category }}] {{ item.title }}
                            </router-link>
                        </td>
                        <td>{{ item.writer }}</td>
                        <td>{{ item.likeCount }}</td>
                        <td>{{ item.views }}</td>
                        <td>{{ item.replyCount }}</td>
                        <td>{{ item.writeDate }}</td>
                    </tr>
                </tbody>
            </table>
        </template>
    </div>
</template>

<script>
export default {
    props:{
        mySort:{
            type:Number,
            required:true
        }
    },
    data() {
        return {
            list:[]
        };
    },
    created() {
        this.$axios({
            url:"/rank/post",
            method:"GET",
            params:{
                sort:this.mySort
            }
        }).then((res)=>{
            if(res.data.status==="S-200"){
                let formatDate = function (date) {
                    let d = new Date(date),
                        month = '' + (d.getMonth() + 1),
                        day = '' + d.getDate(),
                        year = d.getFullYear();
                    if (month.length < 2) 
                        month = '0' + month;
                    if (day.length < 2) 
                        day = '0' + day;
                    return [month, day].join('-');
                };
                this.list = res.data.data.map((e)=>{
                    e.writeDate = formatDate(e.writeDate);
                    e.category = this.$store.getters.categoryNameById(e.categoryId);
                    return e;
                });
            }
        }).catch(e=>{
            console.log(e);
        });
    },
    methods: {
    },
};
</script>

<style>

</style>