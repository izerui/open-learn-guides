<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-layout>
        <v-data-table
                :loading="loading"
                :headers="headers"
                :items="workList"
                :pagination.sync="pagination"
                style="width:100%;height: 100%;"
                class="elevation-1">
            <template v-slot:items="props">
                <td>{{ props.item.ExerciseName }}</td>
                <td class="text-xs-left">
                    <div>
                        {{props.item.StartDate}} 至 {{props.item.EndDate}}
                    </div>
                </td>
                <td class="text-xs-left">
                    <span>{{props.item.SubmitCount}} / {{props.item.MaxTimesOfTrying}} 次</span>
                </td>
                <td class="text-xs-left">{{ props.item.MaxScore }}</td>
                <td class="text-xs-left">
                    <div>
                        <v-btn size="mini" v-if="props.item.state === '2' || props.item.state === '3'"
                               @click="gotoDoWork(props.item)">继续作答
                        </v-btn>
                        <v-btn size="mini" v-else-if="props.item.state === '4'">批阅中</v-btn>
                        <v-btn size="mini" v-else-if="props.item.state === '5'" @click="gotoDoWork(props.item)">重做
                        </v-btn>
                        <v-btn size="mini" v-else @click="gotoDoWork(props.item)">做作业</v-btn>
                    </div>
                </td>
            </template>
        </v-data-table>


        <!-- 试卷详情页 -->
        <v-dialog v-model="paperShow" fullscreen hide-overlay transition="dialog-bottom-transition" scrollable>
            <TestPaper
                    ref="testPaper"
                        @close="paperShow=false"
                       :paper-json="paperJson"
                       :sections="sections"
                       @refresh="refresh"/>
        </v-dialog>
        <dialog-loader ref="dialogLoader"></dialog-loader>
    </v-layout>
</template>

<script>
    // @ is an alias to /src

    import TestPaper from "./TestPaper";
    import DialogLoader from "../components/DialogLoader";

    export default {
        name: 'home',
        components: {TestPaper, DialogLoader},
        data() {
            return {
                workList: [],
                paperShow: false,
                paperJson: {},
                headers: [
                    {text: '作业名称', align: 'left', sortable: false, value: '',},
                    {text: '起止时间', align: 'left', sortable: false, value: ''},
                    {text: '提交次数', align: 'left', sortable: false, value: ''},
                    {text: '最高分', align: 'left', sortable: false, value: ''},
                    {text: '操作', align: 'left', sortable: false, value: ''},
                ],
                pagination: {
                    descending: true,
                    page: 0,
                    rowsPerPage: -1,// -1 for All"
                    sortBy: null,
                    totalItems: null
                },
                loading: false,
                sections: []
            };
        },
        created() {
            this.getMyWork();
        },
        methods: {
            refresh(){
                this.paperShow=false;
                this.getMyWork();
            },
            async getMyWork() {
                this.loading = true;
                const result = await this.$fly.get("/myWork");
                var alist = new Array();
                result.forEach(s => {
                    s.Data.forEach(d => {
                        d.Count = s.Count;
                        d.CourseName = s.CourseName;
                        d.url = s.url;
                        alist.push(d);
                    });
                })
                this.workList = alist;
                this.loading = false;
                console.log("作业列表...", this.workList);
            },
            async gotoDoWork(row) {
                this.$refs.testPaper.reset();
                this.$refs.dialogLoader.show( '打开试卷...', { color: 'primary' } );
                this.dialog = true;
                console.log("点击行", row);
                const url = `http://learn.open.com.cn/StudentCenter/OnLineJob/Redirect?mode=1&courseExerciseID=${row.CourseExerciseID}&submitCount=${row.SubmitCount}&studentHomeworkId=${row.studentHomeworkId}`;
                console.log('跳转试卷页面', url);
                const key = await this.$fly.get("/getTestPagerKey", {url: encodeURI(url)});
                const testPaperUrl = `http://learn.open.com.cn/StudentCenter/OnlineJob/DoHomework?courseExerciseId=${row.CourseExerciseID}&key=${key}&studentHomeworkId=${row.studentHomeworkId}&_=${(new Date()).getTime()}`;
                console.log("请求试卷key", testPaperUrl);
                this.paperJson = await this.$fly.get("/getTestPaper", {url: encodeURI(testPaperUrl)});
                console.log("获取试卷", this.paperJson);
                // 包装 单选、多选下面的对象数组  Sections 对象下 放入 items题目对象
                var sections = this.paperJson.TestPaperContent.Sections;
                const questions = this.paperJson.TestPaperContent.Items;
                sections.forEach(s => {
                    if (!s.items) {
                        s.items = [];
                    }
                    s.ItemID.forEach(id => {
                        questions.forEach(ques => {
                            if (ques.I1 === id) {
                                s.items.push(ques);
                            }
                        })
                    })
                });
                this.sections = sections;
                this.$refs.dialogLoader.hide();
                console.log("包装后", this.paperJson);
                this.paperShow = true;
            }
        }
    }
</script>
