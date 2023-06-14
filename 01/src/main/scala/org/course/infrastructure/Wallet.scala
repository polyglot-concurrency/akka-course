package demo

import akka.actor.typed.*
import akka.actor.typed.scaladsl.Behaviors

object Root:

    trait Command
    final case class GetActor(replyTo: ActorRef[ActorRef[Wallet.Command]]) extends Command

    def apply(w: Option[ActorRef[Wallet.Command]] = None): Behavior[Command] = Behaviors.receive {
      (context, message) =>
        (message, w) match
          case (GetActor(replyTo), None)        =>
            val actor = context.spawnAnonymous(Wallet.apply(List(), List()))
            replyTo ! actor
            apply(Some(actor))
          case (GetActor(replyTo), Some(actor)) =>
            replyTo ! actor
            Behaviors.same
          case _                                => // non intelligent compiler
            context.log.info(s"Default case")
            Behaviors.same
    }

object Wallet:

    case class BalanceResponse(balance: Int)

    trait Command
    final case class Credit(amount: Int)                            extends Command
    final case class Debit(amount: Int)                             extends Command
    final case class GetBalance(replyTo: ActorRef[BalanceResponse]) extends Command

    def apply(credits: List[Int], debits: List[Int]): Behavior[Command] = Behaviors.receive {
      (context, message) =>
        message match
          case Credit(amount)      => apply(amount :: credits, debits)
          case Debit(amount)       => apply(credits, amount :: debits)
          case GetBalance(replyTo) =>
            val total = credits.sum - debits.sum
            context.log.info(s"Actual value: $total")
            replyTo ! BalanceResponse(total)
            Behaviors.same
    }
