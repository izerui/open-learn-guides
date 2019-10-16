<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-card tile>
        <v-toolbar card dark color="primary">
            <v-btn icon dark @click="$emit('close')">
                <v-icon>close</v-icon>
            </v-btn>
            <v-toolbar-title>{{paperJson.ExerciseName}}</v-toolbar-title>
            <v-spacer></v-spacer>
            <span v-if="qesAnswers" style="float: left;">{{qesAnswers.length}}个答案</span>
            <v-toolbar-items>
                <v-btn dark v-if="status === 'unknown'" flat @click="getAnswers">获取答案</v-btn>
                <v-btn dark v-if="status === 'done'" flat @click="saveHomeWork">保存答案</v-btn>
                <v-btn dark v-if="status === 'saved'" flat @click="submitHomeWork">提交作业</v-btn>
            </v-toolbar-items>
        </v-toolbar>

        <v-expansion-panel expand>
            <v-expansion-panel-content v-for="sec in sections">
                <template v-slot:header>
                    <div><h3>{{sec.Title}}</h3></div>
                </template>

                <v-card v-for="(qes,index) in sec.items">
                    <v-card-title primary-title>
                        <span class="title body-2">
                            {{index+1}}.
                        </span>
                        <span class="title body-2" v-html="qes.I2"></span>
                        <v-spacer></v-spacer>
                        <span class="title body-2">{{index+1}}. {{qes.ans}}</span>
                    </v-card-title>
                    <v-card-text class="headline">
                        <span class="grey--text body-2"
                              v-for="ch in qes.Choices">&nbsp;&nbsp;&nbsp;&nbsp;{{ch.I1}}. {{ch.I2}}<br></span>
                    </v-card-text>
                </v-card>
            </v-expansion-panel-content>
        </v-expansion-panel>
        <dialog-loader ref="dialogLoader"></dialog-loader>
    </v-card>
</template>

<script>
    import DialogLoader from "../components/DialogLoader";
    export default {
        name: "TestPaper",
        components: {DialogLoader},
        props: {
            paperJson: {
                type: Object,
                required: true,
                default: {
                    status: 'unknown'
                }
            },
            itemBankId: String,
            homeworkID: String,
            homeworkAnswerId: String,
            sections: {
                type: Array,
                default: []
            }
        },
        data(){
          return {
              qesAnswers: [],
              status: 'unknown',
          }
        },
        computed: {
            answerJson(){
                var answerSheet = {
                    Items: this.qesAnswers.map(q => {
                        var a = {};
                        a.I1 = q.I1;
                        a.I15 = q.I7;
                        a.Sub = [];
                        if (q.Sub) {
                            a.Sub = q.Sub;
                        }
                        return a;
                    })
                };
                return answerSheet;
            }
        },
        created(){
        },
        methods:{
            reset(){
              this.qesAnswers = [];
              this.status = 'unknown';
            },
            async getAnswers() {
                this.$refs.dialogLoader.show( '获取答案...', { color: 'primary' } );
                this.status = 'geting';
                for (var i = 0; i < this.sections.length; i++) {
                    const s = this.sections[i];
                    for (var j = 0; j < s.items.length; j++) {
                        const q = s.items[j];
                        var formData = new FormData;
                        formData.append("itemBankId",this.paperJson.TestPaperContent.Model.P3);
                        formData.append("questionId",q.I1);
                        const res = await this.$fly.post("/getAnswer", formData);
                        res.ans = "答案: ";
                        res.Choices.forEach(ch => {
                            if (ch.IsCorrect) {
                                res.ans += ch.I1;
                                res.ans += "、";
                            }
                        })
                        s.items[j] = res;
                        this.qesAnswers.push(res);
                        // s.items.splice(j, 1, res);
                        // q.Choices = res.Choices;
                    }
                }
                this.status = 'done';
                this.$refs.dialogLoader.hide();
                console.log("答案", this.qesAnswers);
            },
            async saveHomeWork() {
                this.$refs.dialogLoader.show( '保存答案...', { color: 'primary' } );
                var formData = new FormData;
                formData.append("homeworkID",this.paperJson.HomeworkId);
                formData.append("homeworkAnswerId",this.paperJson.HomeworkAnswerId);
                formData.append("answerSheet",JSON.stringify(this.answerJson));
                await this.$fly.put("/saveHomeWork", formData);
                this.$message.success("保存成功,请提交作业!");
                this.status = 'saved';
                this.$refs.dialogLoader.hide();
            },
            async submitHomeWork(){
                this.$refs.dialogLoader.show( '提交作业...', { color: 'primary' } );
                var formData = new FormData;
                formData.append("homeworkID",this.paperJson.HomeworkId);
                formData.append("homeworkAnswerId",this.paperJson.HomeworkAnswerId);
                formData.append("answerSheet",JSON.stringify(this.answerJson));
                this.$fly.put("/submitHomeWork", formData).then(res => {
                    console.log(res);
                    this.$message.success("提交成功,本次分数:" + res.score + '分');
                    this.status = 'submited';
                    this.$emit("refresh");
                    this.$refs.dialogLoader.hide();
                })
            }
        }
    }
</script>

<style scoped>

</style>