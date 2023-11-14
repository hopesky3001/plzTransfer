package com.omapp.plztransfer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.omapp.plztransfer.databinding.ActivityAnalyzeTfactivityBinding
import com.omapp.plztransfer.db.AppDatabase
import com.omapp.plztransfer.db.TFDao
import com.omapp.plztransfer.db.TFEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AnalyzeTFActivity : AppCompatActivity() {

    lateinit var binding: ActivityAnalyzeTfactivityBinding //activity 묶기
    lateinit var db: AppDatabase
    lateinit var tfDao: TFDao // db dao
    lateinit var tfList: ArrayList<TFEntity> //db를 담을 arraylist
    lateinit var scoreList: ArrayList<TFEntity>
    lateinit var percentList: ArrayList<TFEntity>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyzeTfactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAnalyzeBack.setOnClickListener {
            finish()
        }

        db = AppDatabase.getInstance(this)!!
        tfDao = db.getTFDao()

        binding.btnAnalyzeStart.setOnClickListener {
            getTotalAnalyze()
        }
    }


    private fun getTotalAnalyze() {
        val dataFormat = DateTimeFormatter.ofPattern("MM")

        var pmScore: Int = 0
        var pmPercent: Int = 0

        var cScore = 0
        var cPercent = 0

        Thread {
            val today: LocalDate = LocalDate.now()
            val todayFormat = today.format(dataFormat).toInt()

            pmScore = plusMinusScore()
            pmPercent = plusMinusPercent()

            cScore = checkScore()
            cPercent = checkPercent()

            var totalText: String = ""
            var totalTextSum = ""
            var scoreText = ""
            var percentText = ""
            var monthText = ""

            val analyzeTotalText = binding.analyzeTotalText
            val analyzeScoreText = binding.analyzeScoreText
            val analyzePercentText = binding.analyzePercentText


            //month text
            when (todayFormat) {
                1 -> monthText =
                    " 1월입니다. 지금 시험을 보시는 분들은 다음 시험 볼 학교의 문제들을 다시 한번 더 꼭 정리를 하시고 유형들을 숙지하셔서 시험을 볼 수 있도록 합시다." +
                            " 이제 막 시험을 준비하시는 수험생분든 처음부터 너무힘을 주지 마시고 단어들부터 최대한 암기 할 수 있도록합니다. 처음에는 눈으로 훑고 다음 회독때부터는 유의어에 주의하며 효율적으로 외울 수 있도록 합시다."
                2 -> monthText = " 2월입니다. 1월처럼 단어 암기에 유의하면서 문법들을 꼼꼼하게 익히도록 합시다."
                3 -> monthText =
                    " 3월입니다. 전보다는 페이스를 더 올려서 공부량을 늘리고 지난 문법과 단어들을 잊지 않도록 회독을 진행해주도록 합시다. 또한 자신이 원서를 넣을 대학의 이전 해 요구 조건들을 파악해두고 미리 대비할 수 있도록 합시다."
                4 -> monthText =
                    " 4월입니다. 3월과 같이 힘을 내 진행하되 기출과 독해 지문들을 조금씩 풀어보며 시험을 보는 연습을 쌓도록 합시다."
                5 -> monthText =
                    " 5월입니다. 기출문제를 본격적으로 풀기 시작해야하나 조금씩 슬럼프가 올 수 있는 시기입니다. 주에 1~2회는 꼭 휴식을 취하시고 자신의 성적이 낮다고 생각될 경우 너무 비관적으로 빠지지 마시고 당장 자신의 부족한 점을 파악하고 공부하도록 합시다."
                6 -> monthText =
                    " 6월입니다. 기출문제 풀이 수를 점점 더 늘리나 기본적인 단어와 문법에 놓치는 부분이 많다면 우선적으로 다시 암기하고 공부하도록 합시다. 모의고사 또한 꾸준히 보도록 합시다."
                7 -> monthText =
                    " 7월입니다. 더위때문에 점점 공부가 힘들어질 수 있습니다. 기출 풀이는 계속하지만 여전히 성적의 변동이 적을 수 있습니다. 아직 만회 할 수 있는 때이니 포기만 하지 말도록 합시다."
                8 -> monthText =
                    " 8월입니다. 더위와 함께 슬럼프가 올 시기입니다. 무엇을 더 하려고 하지 마시고 그저 자리에 앉아서 단어만이라도 보겠다는 마음가짐으로 버텨주시기 바랍니다. 아무것도 안 하는 날이 점점 많아지면 이후가 더 힘들어집니다."
                9 -> monthText =
                    " 9월입니다. 지금까지 해왔던 것들을 하나둘 정리하기 시작해야합니다. 컨디션 조절에 신경쓰시고 문제량을 늘리고 모의고사를 진행하며 자신이 빠진 부분들을 다시 메우고 휴식도 꼭 취하며 마지막을 위한 준비를 하도록 합시다. "
                10 -> monthText =
                    " 10월입니다. 공부에 더 박차를 가할 때 입니다. 넣고자 하는 학교별로 기출문제를 정리해주시고 학원같은 자체 모의고사를 통해 시험장 분위기에 익숙해지기 바랍니다."
                11 -> monthText =
                    " 11월입니다. 시험이 점점 얼마 남지 않았으니 멘탈 관리에 신경쓰시고 지금까지 해오셨던대로 꾸준히 준비하시기 바랍니다."
                12 -> monthText =
                    " 12월입니다. 최종점검을 하시고 억지로 더 공부량을 늘리기 보다는 몸에 밴 습관처럼 공부를 하시고 건강과 멘탈 관리에 더 유의하시기 바랍니다."
                else -> monthText = "$todayFormat: ERROR"
            }

            //total text
            if (pmScore >= 3 && pmPercent >= 3)
             {
                totalText = "\n 점수와 백분율이 꾸준하게 증가하고 있습니다. 지금 흐름을 놓치지 마시고 이대로만 계속 공부해 주세요."
            } else if (pmScore >= 3
                && pmPercent in 0 until 3
            ) {
                totalText = "\n 점수는 꾸준히 오르고 있지만 백분율은 큰 변화가 보이지 않습니다. " +
                        "그동안 본 시험들이 너무 쉬운 것은 아니었는지, 그로 인해 어려운 문제들을 너무 놓치지 않았는지 다시 체크하도록 하세요."
            } else if (pmScore >= 3
                && pmPercent in -5 until 0
            ) {
                totalText =
                    "\n 점수는 꾸준히 오르고 있지만 백분율은 오히려 떨어지는 추세를 보입니다. 그동안 본 시험들이 너무 쉬운 것은 아니었는지, " +
                            "그로 인해 사소한 실수들이나 고득점의 어려운 문제들을 너무 놓치지 않았는지 다시 체크하도록 하세요."
            } else if (pmScore < 3
                && pmPercent >= 3
            ) {
                totalText = "\n 점수가 오르고 있지 않지만 백분율이 꾸준히 오르고 있는 것이 보입니다. 잘하고 있다는 증거입니다. " +
                        "점수가 낮은 것은 시험이 그만큼 어려웠다는 뜻이니 얽매이지 말고 지금까지 해왔던대로 꾸준히 공부해주세요."
            } else if (pmScore in -1 until 3
                && pmPercent in -5 until 3
            ) {
                totalText =
                    "\n 점수와 백분율의 변화가 적습니다. 점수 유지가 되고있다는 뜻이니 점수대와 백분율이 본인의 희망 대학의 합격 안정권이라면 조급해하지 마시기바랍니다. " +
                            "하지만 성적을 더 올려야 하는 상황이라면 문법에 대한 점검을 하시고 가능한 한 많은 독해 문제를 읽으며 독해력을 늘리도록 합시다."
            } else if (pmScore in -5 until -1
                && pmPercent in -5 .. -1
            ) {
                totalText =
                    "\n 점수와 백분율이 떨어지는 추세입니다. 막 이론강의를 끝낸 상태에서 시험을 보기 시작하면 나올 수 있는 구간입니다. 강의를 듣기만 하는 것은 공부를 하는 것이 아닙니다. " +
                            "꾸준하게 기초 단어와 문법의 회독수를 늘려가며 공부하는 시간을 늘리고 이해가 가지 않는 문법들은 다시 복습하고 질문하며 이해 할 수 있도록 해야 합니다."
            } else if (pmScore <= -10 || pmPercent <= -10) {
                totalText = "\n 저장된 데이터가 부족하거나 불규칙해 판단하기 어렵습니다."
            }
            else {
                totalText = "데이터가 너무 불규칙해 판단하기 어렵습니다."
            }

            //score text
            when (cScore) {
                9 -> scoreText =
                    " 아주 좋은 점수대를 보여주고 있습니다. 시험의 난이도에 따라 백분율의 차이가 있을 수 있겠지만 어느 시험에서든 현재 점수대를 잘 유지한다면 최상위권을 노려볼 만 합니다." +
                            " 실수하지 않는 것을 목표로 현재 루틴을 유지하면서 꾸준히 공부하도록 합시다."
                8 -> scoreText =
                    " 아주 좋은 점수대를 보여주고 있습니다. 시험의 난이도에 따라 백분율의 차이가 있을 수 있겠지만 어느 시험에서든 현재 점수대를 유지한다면  어디든 무난하게 가능할 것입니다." +
                            " 단, 최상위권을 노리고 계신다면 고난이도 문제를 많이 풀어보시고 세세하고 지엽적인 부분들 위주로 꼼꼼하게 공부하시길 바랍니다."
                7 -> scoreText =
                    "상위권에 도전해 볼 수 있는 점수대가 나타나고 있습니다. 상위권을 노리시고 계신다면 모의고사와 기출문제를 많이 풀면서 문법 중 빠진 부분(특히 예외 문법)을 체크하고 독해문제풀이를 늘리고 원서지원을 신중히 합시다." +
                            " 혹은 본인이 이공계 지원자라면 영어는 지금 점수대를 유지하고 수학에 시간 투자를 더 하면 되겠습니다."
                6 -> scoreText =
                    " 인서울 대부분의 중상위 학과나 상위권 중 경쟁률이 낮은 학과에 도전해 볼 만한 점수대입니다. 더 높은 곳을 노리신다면 현재 백분율에 신경쓰지 마시고 단어와 문법부터 점검해보시기 바랍니다. 단어는 상위권의 어려운 단어보다는 자주 출제되는, 잘 안 외워지는 단어부터 암기를 하시고 지금까지 배운 문법을 정확하게 하는 것이 중요합니다." +
                            " 독해는 너무 어려운 문제를 오래 붙잡지 마시고 흘려 읽으며 그 아래 다양한 난이도의 독해 문제들을 확인하시기 바랍니다."
                5 -> scoreText =
                    " 중위권 대학에 도전해 볼 만한 점수입니다. 중위권 대학은 그동안 평균 합격 점수가 그렇게 높지 않으나 편입 시험 자체가 어려운만큼 본인이 노리시고 계시다면 방심하지 마시고 단어와 문법에 대한 회독을 꾸준히 하면서 문제 풀이 수와 독해량을 더 늘리시기 바랍니다."
                0 -> scoreText =
                    " 점수대가 너무 낮거나 아직 표본이 충분하지 않고 패턴이 정형화되어있지 않습니다. 본인의 점수대가 계속 낮게만 나온다면 너무 욕심내어서 진도를 나가고 있는 것이 아닌지 점검해 볼 필요가 있습니다. 편입 영어를 비롯한 한국 수험영어의 기본은 단어에서 나옵니다. 기초 단어부터 꼼꼼히 외우시길 바랍니다."
                -10 -> scoreText = "저장된 데이터가 부족합니다."
                else -> scoreText = "ERROR"

            }

            //percent text
            when (cPercent) {
                1 -> percentText =
                    " 이상적인 백분율을 보여주고 있습니다. 최상위권에 도전 할 수 있고 합격도 충분히 기대해 볼 만 합니다. 고난이도의 문제를 풀며 성적을 유지해주시고 방심해서 실수를 하지 않도록 주의를 하며 꾸준히 공부를 해주시기 바랍니다."
                10 -> percentText =
                    " 좋은 백분율 구간을 보여주고 있습니다. 상위권에 안정적으로 도전하실 수 있으며 최상위권 또한 도전해봄직합니다. 무작정 문제 풀이 수를 늘리기보다 오답 체크를 하며 자주 틀리는 문법 위주로 보고 독해의 정확도와 속도를 회독을 하면서 좀 더 높이는 연습을 하도록 합시다." +
                            " 또한 사회의 굵직은 이슈들을 챙기면서 독해로 나올만한 예상 문제들을 파악해두도록 합시다."
                20 -> percentText =
                    " 좋은 백분율 구간을 보여주고 있습니다. 상위권에 도전해볼 만 하지만 성적을 좀 더 높여야 할 필요가 있습니다. 문제 풀이 수를 늘리거나 오답풀이를 다시 하면서 놓쳤던 단어, 문법을 파악하시고 회독 수를 늘리시기 바랍니다. 독해에서 이해가 가지 않는 논리들 또한 정리하여 다시 확인해서 풀이하시기 바랍니다. " +
                            "이공계 지원자라면 영어는 현재 백분율을 유지해주시고 수학에 집중하도록 합시다."
                30 -> percentText =
                    " 중상위권 정도의 백분율을 보여주고 있습니다. 단어와 문법에서 이따금 불안한 모습을 보일 때가 있을 것입니다. 지난 이론들을 다시 짚어보고 단어 회독을 꾸준히 하도록 합시다." +
                            " 독해 풀이에 시간이 부족하다면 문제 수를 늘리며 지문에 대한 세세한 파악보다 중심 주제를 확실히 잡고 읽어내리는 연습을 해주시기 바랍니다."
                40 -> percentText =
                    " 중위권 정도의 백분율을 보이고 있습니다. 단어와 문법은 어느정도 완성이 되었으나 군데군데 구멍이 많을 때와 같아 보입니다. 고난이도 문제에 집착하지 마시고 기초부터  다시 회독을 하도록 합시다."
                50 -> percentText =
                    " 인서울에 도전 할 수 있는 백분율입니다. 하지만 지금 자신이 무엇을 모르는 지 알지 못할 때 나올 성적대입니다. 지나간 문법 이론들을 처음부터 꼼꼼하게 읽으시고 유의어끼리 묶어 단어를 다시 암기 할 수 있도록 합시다. 회독수를 계속 늘리고 머리에서 빠져나가지 않게 하는 것이 중요합니다." +
                            " 현재 원서접수 기간이고 본인이 이공계라면 상향지원을 더 넣어도 괜찮지만 인문계라면 안전한 곳에 우선적으로 넣으면서 상향지원은 조금만 할 수 있도록 합시다. "
                60 -> percentText =
                    " 중하위권 정도의 백분율을 보이고 있습니다. 본인의 희망대학이 높은 곳이라면은 공부법을 처음부터 다시 정립할 필요가 있습니다. 당장 무작정 모의고사와 기출을 풀면서 문제 풀이 수를 늘리기보다는 단어와 문법의 기초부터 다시 회독할 필요가 있습니다. 하지만 좌절하지는 마시기 바랍니다. 편입은 많은 곳에 원서를 넣을 수 있는 만큼 기회가 많습니다." +
                            " 지금이라도 마음을 잡고 공부를 하신다면 충분히 좋은 곳에 합격할 수 있습니다."
                0 -> percentText =
                    " 백분율이 너무 낮거나 아직 표본이 충분하지 않고 패턴이 정형화되어있지 않습니다. 본인의 점수대가 계속 낮게만 나온다면 너무 욕심내어서 진도를 나가고 있는 것이 아닌지 점검해 볼 필요가 있습니다." +
                            " 편입 영어를 비롯한 한국 수험 영어의 기본은 단어에서 나옵니다. 기초 단어부터 꼼꼼히 외우시길 바랍니다."
                -10 -> percentText = "저장된 데이터가 부족합니다."
                else -> percentText = "ERROR"
            }

            totalTextSum = "$monthText $totalText"

            //해당 텍스트로 ui변경
            runOnUiThread {
                analyzeTotalText.text = totalTextSum
                analyzeScoreText.text = scoreText
                analyzePercentText.text = percentText

                Toast.makeText(this, "분석이 완료되었습니다.", Toast.LENGTH_SHORT).show() //추가되었다는 메시지
            };


        }.start()
    }


    //점수의 증감 정도를 int형 변수로 반환하는 함수
    private fun plusMinusScore(): Int {
        var resultScore = 0                             //return할 결과값
        var scoreCheck = ArrayList(tfDao.getScore())    //DB에서 score값만 뽑아 arrayList로 형성

        var position = scoreCheck.size - 5 //끝에서 5번째 가리키기
        val sizeOfList = scoreCheck.size

        var check = 0.0

        if (sizeOfList >= 5) { //확인해야 할 데이터의 크기가 5보다 클 경우에만 분석진행
            for (i in 1 until 5) {                  //총 4번을 반복하여 5개의 데이터 비교
                check = scoreCheck[position + 1] - scoreCheck[position] //다음 데이터의 점수 - 현재 데이터의 점수
                if (check >= 5) {                       // 다음 데이터의 점수가 5점 이상 높다면
                    resultScore++                       //결과값 +1
                } else if (check <= -5) {                 //다음 데이터의 점수가 5점 이상 낮다면
                    resultScore--                       //결과값 -1
                }                                     //나머지 경우는 무시

                position++                             //비교가 끝나면 다음 데이터 비교를 위해 posistion
                check = 0.0                            //reset
            }
        } else resultScore = -10  //임의의 error handling number : -10 -> "저장된 데이터가 부족합니다."


        return resultScore
    }

    //백분율을 증감 정도를 int형 변수로 반환하는 함수
    private fun plusMinusPercent(): Int {
        var resultPercent = 0
        var percentCheck = ArrayList(tfDao.getPercent())

        var position = percentCheck.size - 5 //끝에서 5번째 가리키기
        val sizeOfList = percentCheck.size

        var check = 0.0

        //분석 시작점
        if (sizeOfList >= 5) { //확인해야 할 데이터의 크기가 5보다 클 경우에만 분석진행
            for (i in 1 until 5) {          //총 4번을 반복하여 5개의 데이터 비교
                check =
                    percentCheck[position + 1] - percentCheck[position] //다음 데이터의 점수 - 현재 데이터의 점수
                if (check >= 3) {                       // 다음 데이터의 백분율이 3점 이상 높다면
                    resultPercent--                       //결과값 -1
                } else if (check <= -3) {                 //다음 데이터의 백분율이 3점 이상 낮다면
                    resultPercent++                       //결과값 +1
                }                                       //나머지 경우는 무시

                position++                             //비교가 끝나면 다음 데이터 비교를 위해 posistion
                check = 0.0                            //reset
            }
        } else resultPercent = -10  //임의의 error handling number : -10 -> "저장된 데이터가 부족합니다."


        return resultPercent
    }

    //절대점수 분석
    private fun checkScore(): Int {
        var resultScore = 0
        var scoreCheck = ArrayList(tfDao.getScore())    //DB에서 score값만 뽑아 arrayList로 형성

        var targetScore = 0
        var position = scoreCheck.size - 5 //끝에서 5번째 가리키기
        val sizeOfList = scoreCheck.size

        var count_90 = 0
        var count_80 = 0
        var count_70 = 0
        var count_60 = 0
        var count_50 = 0
        var count_other = 0

        var level = 0

        //분석 시작점 - 조건: 최근 5개의 점수 중 각 점수 구간이 3개 이상이어야 텍스트 추가
        if (sizeOfList >= 5) { //확인해야 할 데이터의 크기가 5보다 클 경우에만 분석진행
            for (i in 1 .. 5) {          //총 5번을 반복
                targetScore = scoreCheck[position].toInt()

                if (targetScore in 90..100) {
                    count_90++
                    if (count_90 >= 3) {
                        level = 9
                    }
                } else if (targetScore in 80 until 90) {
                    count_80++
                    if (count_80 >= 3) {
                        level = 8
                    }
                } else if (targetScore in 70 until 80) {
                    count_70++
                    if (count_70 >= 3) {
                        level = 7
                    }
                } else if (targetScore in 60 until 70) {
                    count_60++
                    if (count_60 >= 3) {
                        level = 6
                    }
                } else if (targetScore in 50 until 60) {
                    count_50++
                    if (count_50 >= 3) {
                        level = 5
                    }
                } else {
                    count_other++
                    if (count_other >= 3) {
                        level = 0
                    }
                }

                position++                             //비교가 끝나면 다음 데이터 비교를 위해 posistion

            }
        } else level = -10  //임의의 error handling number : -10 -> "저장된 데이터가 부족합니다."


        resultScore = level
        return resultScore
    }

    //절대백분율 분석
    private fun checkPercent(): Int {
        var resultPercent: Int = 0
        var percentCheck = ArrayList(tfDao.getPercent())

        var position = percentCheck.size - 5 //끝에서 5번째 가리키기
        val sizeOfList = percentCheck.size

        var targetPercent = 0

        var count_0 = 0
        var count_1 = 0
        var count_2 = 0
        var count_3 = 0
        var count_4 = 0
        var count_5 = 0
        var count_6 = 0

        var count_other = 0

        var level = 0

        //분석 시작점 - 조건: 최근 5개의 백분율 중 각 점수 구간이 3개 이상이어야 텍스트 추가
        if (sizeOfList >= 5) { //확인해야 할 데이터의 크기가 5보다 클 경우에만 분석진행
            for (i in 1 .. 5) {          //총 5번을 반복
                targetPercent = percentCheck[position].toInt()

                if ((targetPercent > 0) && (targetPercent < 10)) {
                    count_0++
                    if (count_0 >= 3) {
                        level = 1
                    }
                } else if (targetPercent in 10 until 20) {
                    count_1++
                    if (count_1 >= 3) {
                        level = 10
                    }
                } else if (targetPercent in 20 until 30) {
                    count_2++
                    if (count_2 >= 3) {
                        level = 20
                    }
                } else if (targetPercent in 30 until 40) {
                    count_3++
                    if (count_3 >= 3) {
                        level = 30
                    }
                } else if (targetPercent in 40 until 50) {
                    count_4++
                    if (count_4 >= 3) {
                        level = 40
                    }
                } else if (targetPercent in 50 until 60) {
                    count_5++
                    if (count_5 >= 3) {
                        level = 50
                    }
                } else if (targetPercent in 60 until 70) {
                    count_6++
                    if (count_6 >= 3) {
                        level = 60
                    }
                } else {
                    count_other++
                    if (count_other >= 3) {
                        level = 0
                    }
                }

                position++                             //비교가 끝나면 다음 데이터 비교를 위해 posistion

            }
        } else level = -10  //임의의 error handling number : -10 -> "저장된 데이터가 부족합니다."


        resultPercent = level
        return resultPercent
    }
}