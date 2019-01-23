package by.bsuir.levko.salesby.activity.constants

class ShopConstants {

    companion object {
        private val shopsMap = mapOf(
            Pair("виталюр", 168),
            Pair("евроопт", 166),
            Pair("соседи", 171),
            Pair("bigzz", 174),
            Pair("green", 192),
            Pair("mart inn", 191),
            Pair("prostore", 169),
            Pair("spar", 440),
            Pair("азарэнне", 455),
            Pair("алми", 167),
            Pair("белмаркет", 170),
            Pair("бруснічка", 466),
            Pair("гиппо", 164),
            Pair("доброном", 190),
            Pair("домашний", 324),
            Pair("златка", 199),
            Pair("квартал вкуса", 332),
            Pair("коммунарка", 369),
            Pair("копеечка", 494),
            Pair("корона", 177),
            Pair("минскхлебпром", 452),
            Pair("постторг", 178),
            Pair("радзивилловский", 331),
            Pair("родны кут", 538),
            Pair("рублёвский", 165),
            Pair("санта", 516),
            Pair("заводской райпищеторг", 458)
        )

        fun getId(name: String) = shopsMap[name]
    }
}