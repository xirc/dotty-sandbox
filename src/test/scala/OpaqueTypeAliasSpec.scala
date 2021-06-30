import OpaqueTypeAliasSpec.Access.FilePermission

object OpaqueTypeAliasSpec {

  opaque type Logarithm = Double
  object Logarithm:
    def apply(v: Double): Logarithm = math.log(v)
  extension (x: Logarithm)
    def toDouble: Double = math.exp(x)
    def *(y: Logarithm): Logarithm = x + y
    def show: String = f"Logarithm(${math.exp(x)}%.1f)"

  object Access {

    opaque type FilePermission <: UserPermission & GroupPermission & OthersPermission = Int
    opaque type UserPermission <: Permission = Int
    opaque type GroupPermission <: Permission = Int
    opaque type OthersPermission <: Permission = Int

    object FilePermission:
      def apply(
          user: UserPermission,
          group: GroupPermission = No,
          others: OthersPermission = No,
      ): FilePermission =
        user | group | others
      def show(x: FilePermission): String =
        Permission.show(x.user) + Permission.show(x.group) + Permission.show(x.others)
      extension (x: FilePermission)
        def user: UserPermission = (x & 0x1c0) >> 6 // 1_1100_0000
        def group: GroupPermission = (x & 0x038) >> 3 // 0_0011_1000
        def others: OthersPermission = x & 0x007 // 0_0000_0111

    opaque type Permission = Int
    val R: Permission = 4
    val W: Permission = 2
    val X: Permission = 1
    val No: Permission = 0

    object Permission:
      def user(x: Permission): UserPermission = (x & 0x007) << 6
      def group(x: Permission): GroupPermission = (x & 0x007) << 3
      def others(x: Permission): OthersPermission = (x & 0x007)
      def show(x: Permission): String = {
        val sr = if (x & R) != 0 then "r" else "-"
        val sw = if (x & W) != 0 then "w" else "-"
        val sx = if (x & X) != 0 then "x" else "-"
        sr + sw + sx
      }
      extension (x: Permission) def &(y: Permission): Permission = x | y

  }

}

/** [[https://dotty.epfl.ch/docs/reference/other-new-features/opaques.html Opaque Type Alias]] */
final class OpaqueTypeAliasSpec extends BaseSpec {
  import OpaqueTypeAliasSpec.*

  "Logarithm" in {

    val x = Logarithm(2) // log_e 2
    val y = Logarithm(3) // log_e 3
    val z = x * y // log_e 6

    assert(z == Logarithm(6))
    assert(z.toDouble == 6)
    assert(z.show == "Logarithm(6.0)")

  }

  "Access" in {

    import Access.*
    import Access.Permission.*

    val _740 = FilePermission(user(R & W & X), group(R), others(No))
    assert(FilePermission.show(_740) == "rwxr-----")
    assert(Permission.show(_740.user) == "rwx")
    assert(Permission.show(_740.group) == "r--")
    assert(Permission.show(_740.others) == "---")

    val _777 = FilePermission(user(R & W & X), group(R & W & X), others(R & W & X))
    assert(FilePermission.show(_777) == "rwxrwxrwx")
    assert(Permission.show(_777.user) == "rwx")
    assert(Permission.show(_777.group) == "rwx")
    assert(Permission.show(_777.others) == "rwx")

  }

}
