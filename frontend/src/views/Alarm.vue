<template>
    <div>
        <div v-for="(alarm, index) in alarmList" :key="index">
            <div>
                {{alarm}}
            </div>
            <button @click="check(alarm.id)">click!</button>
        </div>
    </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
    name:"Alarm",
    data(){
        return{
            alarmList:[],
        }
    },
    created(){
        this.alarmList = this.$route.params.alarmList;
        console.log(this.alarmList);
    },
    computed:{
        ...mapGetters(["id"])
    },
    methods:{
        check(alarmId){
            
            this.$route.params.alarmStomp.send(
                "/server/checkAlarm",
                JSON.stringify({
                    memberId: this.id,
                    alarmId:alarmId
                }),
                {}
            )

            this.alarmList = this.alarmList.filter(x => x.id != alarmId);

        },
        checkAll(){
            
            this.$route.params.alarmStomp.send(
                "/server/checkAll",
                JSON.stringify({
                    memberId: this.id
                }),
                {}
            )

            this.alarmList = [];

        },
        goTodo(todoId){
            // 해당 Todo의 상세 페이지로 이동
            // this.$route.push("/todoInfo");
            console.log("go to",todoId);
        }
    }
}
</script>

<style>

</style>