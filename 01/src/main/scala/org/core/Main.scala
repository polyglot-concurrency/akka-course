package demo

import akka.actor.typed.*
import akka.actor.typed.scaladsl.AskPattern._
import scala.concurrent.duration.*
import akka.util.Timeout
import scala.concurrent.Future

object in:
    given timeout: Timeout = 3.seconds
    given r: ActorSystem[Root.Command] = ActorSystem(Root(), "system")
    given ec: scala.concurrent.ExecutionContext = r.executionContext

    def getWallet()(using guardian: ActorSystem[Root.Command]): Future[ActorRef[Wallet.Command]] = guardian ? (Root.GetActor(_))

    def getBalance(wallet: ActorRef[Wallet.Command]): Future[Wallet.BalanceResponse] = wallet ? (Wallet.GetBalance(_))

    def printBalance: Unit =
      for
          wallet <- getWallet()
          response <- getBalance(wallet)
      do println(s"Balance: ${response.balance}")

    def addCredit(c: Int): Unit = for wallet <- getWallet() do wallet ! Wallet.Credit(c)

    def addDebit(c: Int): Unit = for wallet <- getWallet() do wallet ! Wallet.Debit(c)
