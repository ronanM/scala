/*     ____ ____  ____ ____  ______                                     *\
**    / __// __ \/ __// __ \/ ____/    SOcos COmpiles Scala             **
**  __\_ \/ /_/ / /__/ /_/ /\_ \       (c) 2002, LAMP/EPFL              **
** /_____/\____/\___/\____/____/                                        **
\*                                                                      */

// $Id$

package meta.scalac.ast;

import java.util.List;
import java.util.ArrayList;

import meta.java.Type;
import meta.scalac.Phase;

/** This class describes all tree nodes. */
public class Tree {

    //########################################################################
    // Public Constants

    public static final String PACKAGE  = "scalac.ast";
    public static final String NAME     = "Tree";

    //########################################################################
    // Private Constants

    private static final TreeKind Any  = TreeKind.Any;
    private static final TreeKind Type = TreeKind.Type;
    private static final TreeKind Term = TreeKind.Term;
    private static final TreeKind Dual = TreeKind.Dual;
    private static final TreeKind Test = TreeKind.Test;
    private static final TreeKind None = TreeKind.None;

    private static final TreeSymbol NoSym  = TreeSymbol.NoSym;
    private static final TreeSymbol HasSym = TreeSymbol.HasSym;
    private static final TreeSymbol DefSym = TreeSymbol.DefSym;

    private static final TreeFieldLink SymFlags = TreeFieldLink.SymFlags;
    private static final TreeFieldLink SymName  = TreeFieldLink.SymName;

    private final List list = new ArrayList();

    //########################################################################
    // Public Constants

    public final TreeNode
        n_Bad            = node("Bad"           , Any , HasSym),
        n_Empty          = node("Empty"         , Any , NoSym),
        n_ClassDef       = node("ClassDef"      , None, DefSym),
        n_PackageDef     = node("PackageDef"    , None, NoSym),
        n_ModuleDef      = node("ModuleDef"     , None, DefSym),
        n_ValDef         = node("ValDef"        , None, DefSym),
        n_PatDef         = node("PatDef"        , None, NoSym),
        n_DefDef         = node("DefDef"        , None, DefSym),
        n_TypeDef        = node("TypeDef"       , None, DefSym),
        n_Import         = node("Import"        , None, HasSym),
        n_CaseDef        = node("CaseDef"       , None, NoSym),
        n_Template       = node("Template"      , None, HasSym), // !!! HasSym
        n_LabelDef       = node("LabelDef"      , Term, DefSym),
        n_Block          = node("Block"         , Term, NoSym),
        n_Tuple          = node("Tuple"         , Term, NoSym),
        n_Visitor        = node("Visitor"       , Term, NoSym),
        n_Function       = node("Function"      , Term, NoSym),
        n_Assign         = node("Assign"        , Term, NoSym),
        n_If             = node("If"            , Term, NoSym),
        n_New            = node("New"           , Term, NoSym),
        n_Typed          = node("Typed"         , Term, NoSym),
        n_TypeApply      = node("TypeApply"     , Term, NoSym),
        n_Apply          = node("Apply"         , Term, NoSym),
        n_Super          = node("Super"         , Term, NoSym),
        n_This           = node("This"          , Term, NoSym),
        n_Select         = node("Select"        , Test, HasSym),
        n_Ident          = node("Ident"         , Test, HasSym),
        n_Literal        = node("Literal"       , Term, NoSym),
        n_TypeTerm       = node("TypeTerm"      , Type, NoSym),
        n_SingletonType  = node("SingletonType" , Type, NoSym),
        n_SelectFromType = node("SelectFromType", Type, HasSym),
        n_FunType        = node("FunType"       , Type, NoSym),
        n_CompoundType   = node("CompoundType"  , Type, NoSym),
        n_AppliedType    = node("AppliedType"   , Type, NoSym),
        n_CovariantType  = node("CovariantType" , Type, NoSym);

    public final TreeNode[] nodes;

    public final Type
        t_int       = TreeType.INT,
        t_Object    = TreeType.Reference(null, "Object"),
        t_Global    = TreeType.Reference("scalac", "Global"),
        t_Unit      = TreeType.Reference("scalac", "Unit"),
        t_TreeGen   = TreeType.Reference("scalac.ast", "TreeGen"),
        t_Symbol    = TreeType.Reference("scalac.symtab", "Symbol"),
        t_Type      = TreeType.Reference("scalac.symtab", "Type"),
        t_Debug     = TreeType.Reference("scalac.util", "Debug"),
        t_Name      = TreeType.Name(Any),
        t_TypeName  = TreeType.Name(Type),
        t_TermName  = TreeType.Name(Term),
        t_TestName  = TreeType.Name(Test),
        t_Names     = TreeType.Array(t_Name),
        t_Tree      = TreeType.Tree(Any),
        t_TypeTree  = TreeType.Tree(Type),
        t_TermTree  = TreeType.Tree(Term),
        t_Trees     = TreeType.Array(t_Tree),
        t_TypeTrees = TreeType.Array(t_TypeTree),
        t_TermTrees = TreeType.Array(t_TermTree),
        t_Treess    = TreeType.Array(t_Trees),
        t_ValDef    = n_ValDef.getType(0),
        t_ValDefs   = n_ValDef.getType(1),
        t_ValDefss  = n_ValDef.getType(2),
        t_TypeDef   = n_TypeDef.getType(0),
        t_TypeDefs  = n_TypeDef.getType(1),
        t_Template  = n_Template.getType(0),
        t_CaseDef   = n_CaseDef.getType(0),
        t_CaseDefs  = n_CaseDef.getType(1),
        t_Ident     = n_Ident.getType(0),
        t_Idents    = n_Ident.getType(1);

    //########################################################################
    // Public Constructors

    public Tree() {
        nodes = (TreeNode[])list.toArray(new TreeNode[list.size()]);

        n_Bad.
            setDescription("Representation for parser errors").
            setRange(Phase.PARSER, Phase.END);

        n_Empty.
            setDescription("A tree node for the absence of a tree").
            setRange(Phase.PARSER, Phase.UNKNOWN).
            noFields();

        n_ClassDef.
            setDescription("Class and data declaration").
            setRange(Phase.PARSER, Phase.END).
            addField(t_int, "mods", SymFlags).
            addField(t_TypeName, "name", SymName).
            addField(t_TypeDefs, "tparams").
            addField(t_ValDefss, "vparams").
            addField(t_TypeTree, "tpe").
            addField(t_Template, "impl");

        n_PackageDef.
            setDescription("Package declaration").
            setRange(Phase.PARSER, Phase.UNKNOWN).
            addField(t_TermTree, "packaged").
            addField(t_Template, "impl");

        n_ModuleDef.
            setDescription("Module declaration").
            setRange(Phase.PARSER, Phase.UNKNOWN).
            addField(t_int, "mods", SymFlags).
            addField(t_TermName, "name", SymName).
            addField(t_TypeTree, "tpe").
            addField(t_Template, "impl");

        n_ValDef.
            setDescription("Value declaration (var or let)").
            setRange(Phase.PARSER, Phase.END).
            addField(t_int, "mods", SymFlags).
            addField(t_TermName, "name", SymName).
            addField(t_TypeTree, "tpe").
            addField(t_TermTree, "rhs");


        n_PatDef.
            setDescription("Value declaration with patterns (val)").
            setRange(Phase.PARSER, Phase.DESUGARIZER).
            addField(t_int, "mods").
            addField(t_TermTree, "pat").
            addField(t_TermTree, "rhs");

        n_DefDef.
            setDescription("Function declaration (def)").
            setRange(Phase.PARSER, Phase.END).
            addField(t_int, "mods", SymFlags).
            addField(t_TermName, "name", SymName).
            addField(t_TypeDefs, "tparams").
            addField(t_ValDefss, "vparams").
            addField(t_TypeTree, "tpe").
            addField(t_TermTree, "rhs");

        n_TypeDef.
            setDescription("Type declaration").
            setRange(Phase.PARSER, Phase.ERASURE). // !!! could/should be removed earlier?)
            addField(t_int, "mods", SymFlags).
            addField(t_TypeName, "name", SymName).
            addField(t_TypeTree, "rhs");

        n_Import.
            setDescription("Import declaration").
            setRange(Phase.START, Phase.ANALYZER).
            addField(t_TermTree, "expr").
            addField(t_Names, "selectors");

        n_CaseDef.
            setDescription("Case declaration").
            setRange(Phase.PARSER, Phase.UNKNOWN).
            addField(t_TermTree, "pat").
            addField(t_TermTree, "guard").
            addField(t_TermTree, "body");

        n_Template.
            setDescription("Instantiation templates").
            setRange(Phase.PARSER, Phase.END).
            addField(t_TermTrees, "parents").
            addField(t_Trees, "body");

        n_LabelDef.
            setDescription("Labelled expression - the symbols in the array (must be Idents!) are those the label takes as argument").
            setRange(Phase.OPTIMIZER, Phase.END).
            addField(t_TermTrees, "params"). // !!! shoudl be t_Idents
            addField(t_TermTree, "rhs");

        n_Block.
            setDescription("Block of expressions " +
                "(semicolon separated expressions)").
            setRange(Phase.PARSER, Phase.END).
            addField(t_Trees, "stats");

        n_Tuple.
            setDescription("Tuple of expressions (comma separated expressions)").
            setRange(Phase.UNCURRY, Phase.LAMBDALIFT).
            addField(t_TermTrees, "trees");

        n_Visitor.
            setDescription("Visitor (a sequence of cases)").
            setRange(Phase.PARSER, Phase.TRANSMATCH).
            addField(t_CaseDefs, "cases");


        n_Function.
            setDescription("Anonymous function").
            setRange(Phase.PARSER, Phase.ANALYZER).
            addField(t_ValDefs, "vparams").
            addField(t_TermTree, "body");

        n_Assign.
            setDescription("Assignment").
            setRange(Phase.PARSER, Phase.END).
            addField(t_TermTree, "lhs").
            addField(t_TermTree, "rhs");

        n_If.
            setDescription("Conditional expression").
            setRange(Phase.PARSER, Phase.END).
            addField(t_TermTree, "cond").
            addField(t_TermTree, "thenp").
            addField(t_TermTree, "elsep");

        n_New.
            setDescription("Instantiation").
            setRange(Phase.PARSER, Phase.END).
            addField(t_Template, "templ");

        n_Typed.
            setDescription("Type annotation").
            setRange(Phase.PARSER, Phase.UNKNOWN). // !!! could be removed by analyzer?
            addField(t_TermTree, "expr").
            addField(t_TypeTree, "tpe");


        n_TypeApply.
            setDescription("Type application").
            setRange(Phase.PARSER, Phase.END).
            addField(t_TermTree, "fun").
            addField(t_TypeTrees, "args");

        n_Apply.
            setDescription("Value application").
            setRange(Phase.PARSER, Phase.END).
            addField(t_Tree, "fun"). // !!! should be t_TermTree
            addField(t_TermTrees, "args");

        n_Super.
            setDescription("Super reference").
            setRange(Phase.PARSER, Phase.END).
            addField(t_TypeTree, "tpe");

        n_This.
            setDescription("Self reference").
            setRange(Phase.PARSER, Phase.END).
            addField(t_TypeTree, "qualifier");

        n_Select.
            setDescription("Designator").
            setRange(Phase.START, Phase.END).
            addField(t_TermTree, "qualifier").
            addField(t_TestName, "selector", SymName);

        n_Ident.
            setDescription("Identifier").
            setRange(Phase.START, Phase.END).
            addField(t_TestName, "name", SymName);

        n_Literal.
            setDescription("Literal").
            setRange(Phase.PARSER, Phase.END).
            addField(t_Object, "value");

        n_TypeTerm.
            setDescription("TypeTerm").
            setRange(Phase.PARSER, Phase.END);

        n_SingletonType.
            setDescription("Singleton type").
            setRange(Phase.PARSER, Phase.ANALYZER).
            addField(t_TermTree, "ref");

        n_SelectFromType.
            setDescription("Type selection").
            setRange(Phase.PARSER, Phase.ANALYZER).
            addField(t_TypeTree, "qualifier").
            addField(t_TypeName, "selector", SymName);

        n_FunType.
            setDescription("Function type").
            setRange(Phase.PARSER, Phase.ANALYZER).
            addField(t_TypeTrees, "argtpes").
            addField(t_TypeTree, "restpe");

        n_CompoundType.
            setDescription("Object type (~ Template)").
            setRange(Phase.PARSER, Phase.ANALYZER).
            addField(t_TypeTrees, "parents").
            addField(t_Trees, "refinements");

        n_AppliedType.
            setDescription("Applied type").
            setRange(Phase.PARSER, Phase.ANALYZER).
            addField(t_TypeTree, "tpe").
            addField(t_TypeTrees, "args");

        n_CovariantType.
            setDescription("Covariant type").
            setRange(Phase.PARSER, Phase.ANALYZER).
            addField(t_TypeTree, "tpe");

    }

    //########################################################################
    // Public Functions

    public static boolean isTree(Type type) {
        switch (type) {
        case Array(Type item):
            return isTree(item);
        case TreeType.Tree(_):
        case TreeType.Node(_):
            return true;
        default:
            return false;
        }
    }

    //########################################################################
    // Private Methods

    private TreeNode node(String name, TreeKind kind, TreeSymbol symbol) {
        TreeNode node = new TreeNode(name, kind, symbol);
        list.add(node);
        return node;
    }

    //########################################################################
}
