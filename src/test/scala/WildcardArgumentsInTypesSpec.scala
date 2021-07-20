/** [[https://dotty.epfl.ch/docs/reference/changed-features/wildcards.html Wildcard Arguments in Types]]
  */
final class WildcardArgumentsInTypesSpec extends BaseSpec {

  // Use '?' instead of '_'
  def length(xs: List[?]): Int = xs.size

  "? wildcard arguments" in {

    assert(length(List(1, 2, 3)) == 3)

  }

}
